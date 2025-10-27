package com.inyro.api.global.security.exception;


import com.inyro.api.global.apiPayload.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}
