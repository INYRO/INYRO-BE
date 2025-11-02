package com.inyro.api.domain.reservation.service.lock;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationLockService {
    void validateTimeLock(LocalDate date, LocalTime start, LocalTime end, String sno);
    boolean acquireLock(LocalDate date, LocalTime time, String sno);
    void deleteTimeLock(LocalDate date, LocalTime start, LocalTime end);
}
