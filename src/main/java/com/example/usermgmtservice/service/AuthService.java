package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.auth.LoginRequest;
import com.example.usermgmtservice.model.auth.RegisterRequest;
import com.example.usermgmtservice.model.auth.TokenResponse;
import com.example.usermgmtservice.model.auth.UserResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthService {
    Mono<Void> createKeycloakUser(RegisterRequest request, UUID userId);
    Mono<TokenResponse> login(LoginRequest request);
}
