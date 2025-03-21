package com.example.usermgmtservice.web.fn;

import com.example.usermgmtservice.model.auth.LoginRequest;
import com.example.usermgmtservice.model.auth.LogoutRequest;
import com.example.usermgmtservice.model.auth.RegisterRequest;
import com.example.usermgmtservice.model.exception.AuthException;
import com.example.usermgmtservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthService authService;

        public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
            .flatMap(authService::register)
            .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> login(ServerRequest request){
        return request.bodyToMono(LoginRequest.class)
            .flatMap(authService::login)
            .flatMap(tokenResponse -> ServerResponse.status(HttpStatus.OK).bodyValue(tokenResponse))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> logout(ServerRequest request){
        return request.bodyToMono(LogoutRequest.class)
            .flatMap(authService::logout)
            .then(Mono.defer(() -> ServerResponse.status(HttpStatus.NO_CONTENT).build()))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }
}
