package com.example.usermgmtservice.model.auth;

public record AccessTokenResponse(
    String access_token,
    Long expires_in
) {}
