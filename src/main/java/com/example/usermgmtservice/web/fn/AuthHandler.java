package com.example.usermgmtservice.web.fn;

import com.example.usermgmtservice.model.auth.*;
import com.example.usermgmtservice.model.exception.AuthException;
import com.example.usermgmtservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthService authService;

    @Value("${https}")
    private boolean isHttps;

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
            .flatMap(authService::register)
            .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
            .flatMap(authService::login)
            .flatMap(tokenResponse -> {
                ResponseCookie refreshCookie = buildRefreshCookie(tokenResponse.refresh_token(), tokenResponse.refresh_expires_in());

                AccessTokenResponse response = new AccessTokenResponse(
                    tokenResponse.access_token(),
                    tokenResponse.expires_in()
                );

                return ServerResponse.ok()
                    .cookie(refreshCookie)
                    .bodyValue(response);
            })
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        return request.bodyToMono(LogoutRequest.class)
            .flatMap(authService::logout)
            .then(Mono.defer(() -> ServerResponse.status(HttpStatus.NO_CONTENT).build()))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> refreshToken(ServerRequest request) {
        return Mono.justOrEmpty(request.cookies().getFirst("refresh_token"))
            .switchIfEmpty(Mono.error(new AuthException(HttpStatus.UNAUTHORIZED, "Refresh token not found")))
            .flatMap(refreshToken -> authService.refreshToken(new RefreshTokenRequest(refreshToken.getValue())))
            .flatMap(tokenResponse -> {
                AccessTokenResponse response = new AccessTokenResponse(
                    tokenResponse.access_token(),
                    tokenResponse.expires_in()
                );
                ResponseCookie refreshCookie = buildRefreshCookie(tokenResponse.refresh_token(), tokenResponse.refresh_expires_in());

                return ServerResponse.ok()
                    .cookie(refreshCookie)
                    .bodyValue(response);
            })
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public ResponseCookie buildRefreshCookie(String refreshToken, Long expiresIn) {
        return ResponseCookie.from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(isHttps) // Set to true if using HTTPS
            .path("/")
            .sameSite("Lax")
            .maxAge(expiresIn)
            .build();
    }
}
