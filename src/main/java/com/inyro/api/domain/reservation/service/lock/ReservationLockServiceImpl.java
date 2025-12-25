package com.inyro.api.domain.reservation.service.lock;

import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationLockServiceImpl implements ReservationLockService {

    private final RedisUtils<String, String> redisUtils;

    private static final long LOCK_TTL_SECONDS = 300L;

    @Override
    public void validateTimeLock(LocalDate date, LocalTime start, LocalTime end, String sno) {
        LocalTime time = start;
        while (time.isBefore(end)) {
            if (!Objects.equals(redisUtils.get(date + ":" + time), sno)) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_LOCK_FAILED);
            }
            time = time.plusMinutes(30);
        }
    }

    @Override
    public boolean acquireLock(LocalDate date, LocalTime time, String sno) {
        String key = generateKey(date, time);
        return redisUtils.lock(key, sno, LOCK_TTL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void deleteTimeLock(LocalDate date, LocalTime start, LocalTime end) {
        // 예약 완료 후 시작부터 끝까지 락 해제
        while(start.isBefore(end)) {
            redisUtils.delete(generateKey(date, start));
            start = start.plusMinutes(30);
        }
    }

    @Override
    public String getLockValue(LocalDate date, LocalTime time) {
        return redisUtils.get(generateKey(date, time));
    }

    private String generateKey(LocalDate date, LocalTime time) {
        return date + ":" + time;
    }
}
