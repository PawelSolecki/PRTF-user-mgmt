package com.example.usermgmtservice.model.auth;

public record LoginRequest(
    String email,
    String password
) {
}
