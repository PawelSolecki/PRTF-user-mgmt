package com.example.usermgmtservice.service;

import com.example.usermgmtservice.domain.User;
import com.example.usermgmtservice.mapper.UserMapper;
import com.example.usermgmtservice.model.auth.*;
import com.example.usermgmtservice.model.exception.AuthException;
import com.example.usermgmtservice.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceKeycloakImpl implements AuthService {

    private final Keycloak keycloakAdminClient;

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Override
    public Mono<UserResponse> register(RegisterRequest request) {
        return userRepository.findByEmail(request.email())
            .flatMap(existingUser ->
                Mono.<UserResponse>error(new AuthException(HttpStatus.CONFLICT, "User exists"))
            )
            .switchIfEmpty(Mono.defer(() ->
                createKeycloakUser(request)
                    .flatMap(keycloakUserId -> {
                        User userEntity = userMapper.toEntity(request);
                        userEntity.setId(UUID.fromString(keycloakUserId));
                        userEntity.setNew(true);
                        log.info("User created with keycloak id: {}", keycloakUserId);
                        log.info("User: " + userEntity);
                        return userRepository.save(userEntity)
                            .map(savedUser -> {
                                    UserResponse ur = new UserResponse(
                                        savedUser.getId(),
                                        savedUser.getName(),
                                        savedUser.getEmail()
                                    );
                                    log.info("User saved with id: {}", savedUser.getId());
                                    return ur;
                                }
                            );
                    })
            ));
    }

    public Mono<String> createKeycloakUser(RegisterRequest request) {
        return Mono.fromCallable(() -> {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.email());
            user.setEmail(request.email());
            user.setFirstName(request.name());
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.password());
            credential.setTemporary(false);

            user.setCredentials(List.of(credential));

            try (Response response = keycloakAdminClient.realm(realm).users().create(user)) {
                if (response.getStatus() != 201) {
                    throw new AuthException(HttpStatus.INTERNAL_SERVER_ERROR, "Keycloak user creation failed");
                }

                String location = response.getLocation().getPath();
                return location.substring(location.lastIndexOf('/') + 1);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest request) {
        return webClient.post()
            .uri(authServerUrl + "/realms/{realm}/protocol/openid-connect/token", realm)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(
                "grant_type=password&" +
                    "client_id=" + clientId + "&" +
                    "client_secret=" + clientSecret + "&" +
                    "username=" + request.email() + "&" +
                    "password=" + request.password()
            )
            .retrieve()
            .bodyToMono(TokenResponse.class);
    }

    @Override
    public Mono<Void> logout(LogoutRequest request) {
        return webClient.post()
            .uri(authServerUrl + "/realms/{realm}/protocol/openid-connect/logout", realm)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(
                "client_id=" + clientId + "&" +
                    "client_secret=" + clientSecret + "&" +
                    "refresh_token=" + request.refreshToken()
            )
            .retrieve()
            .toBodilessEntity()
            .then();
    }

    @Override
    public Mono<TokenResponse> refreshToken(RefreshTokenRequest request) {
        return webClient.post()
            .uri(authServerUrl + "/realms/{realm}/protocol/openid-connect/token", realm)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(
                "grant_type=refresh_token&" +
                    "client_id=" + clientId + "&" +
                    "client_secret=" + clientSecret + "&" +
                    "refresh_token=" + request.refreshToken()
            )
            .retrieve()
            .bodyToMono(TokenResponse.class);
    }
}
