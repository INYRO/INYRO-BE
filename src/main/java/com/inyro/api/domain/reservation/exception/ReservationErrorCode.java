package com.inyro.api.domain.reservation.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements BaseErrorCode {
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "R001", "이미 해당 시간대에 예약이 존재합니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
