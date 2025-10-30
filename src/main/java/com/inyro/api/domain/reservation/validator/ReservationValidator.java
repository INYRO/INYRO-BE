package com.inyro.api.domain.reservation.validator;

import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ReservationValidator {

    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);

    public void validateTimeRange(LocalTime start, LocalTime end) {
        if (start.isBefore(OPENING_TIME) || end.isAfter(CLOSING_TIME)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_OUT_OF_RANGE);
        }
    }
}
