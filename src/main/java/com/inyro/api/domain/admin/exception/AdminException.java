package com.inyro.api.domain.admin.exception;

import com.inyro.api.global.apiPayload.exception.CustomException;

public class AdminException extends CustomException {
    public AdminException(AdminErrorCode errorCode) {
        super(errorCode);
    }
}
