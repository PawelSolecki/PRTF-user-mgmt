package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.auth.LoginRequest;
import com.example.usermgmtservice.model.auth.RegisterRequest;
import com.example.usermgmtservice.model.auth.TokenResponse;
import com.example.usermgmtservice.model.auth.UserResponse;
import com.example.usermgmtservice.model.exception.AuthException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloakAdminClient;

    private final WebClient webClient;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Override
    public Mono<Void> createKeycloakUser(RegisterRequest request, UUID userId) {
        return Mono.fromCallable(() -> {
            UserRepresentation user = new UserRepresentation();
            user.setId(userId.toString());
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
                return null;
            }

        });
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
}
