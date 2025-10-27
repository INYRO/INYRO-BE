package com.inyro.api.domain.reservation.exception;

import com.inyro.api.global.apiPayload.exception.CustomException;

public class ReservationException extends CustomException {
    public ReservationException(ReservationErrorCode errorCode) {
        super(errorCode);
    }
}
