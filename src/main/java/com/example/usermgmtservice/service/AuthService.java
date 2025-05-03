package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.auth.*;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<UserResponse> register(RegisterRequest request);
    Mono<TokenResponse> login(LoginRequest request);

    Mono<Void> logout(LogoutRequest request);

    Mono<TokenResponse> refreshToken(RefreshTokenRequest request);
}
