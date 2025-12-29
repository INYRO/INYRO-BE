package com.inyro.api.domain.reservation.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements BaseErrorCode {
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "R409", "이미 해당 시간대에 예약이 존재하거나 다른 사용자가 예약 중입니다."),
    RESERVATION_TIME_LOCK_FAILED(HttpStatus.FORBIDDEN, "R403", "해당 시간대에 예약을 할 락을 획득하지 못했습니다."),
    RESERVATION_TIME_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "R400", "해당 시간대는 예약이 불가능합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R404", "해당 예약을 찾을 수 없습니다."),
    RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "R403", "해당 예약에 대한 권한이 없습니다."),
    RESERVATION_LOCK_FORBBIEN(HttpStatus.FORBIDDEN, "R403", "해당 락에 대한 권한이 없습니다."),
    RESERVATION_DATE_PAST(HttpStatus.FORBIDDEN, "R403", "이미 지난 날짜는 선택할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
