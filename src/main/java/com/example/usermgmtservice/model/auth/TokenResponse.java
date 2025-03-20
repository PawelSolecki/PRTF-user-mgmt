package com.example.usermgmtservice.model.auth;

public record TokenResponse(String access_token, String refresh_token, Long expires_in, Long refresh_expires_in) {
}
