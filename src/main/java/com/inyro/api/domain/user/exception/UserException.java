package com.inyro.api.domain.user.exception;

import com.inyro.api.global.apiPayload.exception.CustomException;

public class UserException extends CustomException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
