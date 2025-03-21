package com.example.usermgmtservice.model.auth;

public record LogoutRequest(
    String refreshToken
) {
}
