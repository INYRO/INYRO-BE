package com.inyro.api.domain.reservation.validator;

import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final RedisUtils<String, String> redisUtils;

    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);

    public void validateTimeRange(LocalTime start, LocalTime end) {
        if (start.isBefore(OPENING_TIME) || end.isAfter(CLOSING_TIME)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_OUT_OF_RANGE);
        }
    }

    public void validateTimeLock(LocalDate date, LocalTime start, LocalTime end, String sno) {
        LocalTime time = start;
        while (time.isBefore(end)) {
            if (!Objects.equals(redisUtils.get(date + ":" + time), sno)) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_LOCK_FAILED);
            }
            time = time.plusMinutes(30);
        }
    }
}
