package com.example.usermgmtservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthException extends CustomException {
    public AuthException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
