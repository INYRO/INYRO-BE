package com.inyro.api.domain.reservation.service;

import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import com.inyro.api.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCalculator {

    private final ReservationRepository reservationRepository;
    private final RedisUtils<String, String> redisUtils;
    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);

    public Map<LocalTime, Boolean> calculateAvailableSlots(LocalDate date) {

        List<LocalTime> allSlots = generateSlots();
        Set<LocalTime> reservationTimeSet = generateTimeSetFromReservations(date);
        Set<LocalTime> redisTimeSet = generateTimeSetFromRedis(date, allSlots);
        // TimeSet 병합
        Set<LocalTime> mergedTimeSet = new HashSet<>(reservationTimeSet);
        mergedTimeSet.addAll(redisTimeSet);

        // 순서가 보장되는 맵으로 예약 가능한 시간을 표시
        Map<LocalTime, Boolean> availableMap = new LinkedHashMap<>();
        for (LocalTime time : allSlots) {
            availableMap.put(time, !mergedTimeSet.contains(time));
        }
        return availableMap;
    }

    // Redis 락에서 시간 추출
    private Set<LocalTime> generateTimeSetFromRedis(LocalDate date, List<LocalTime> slots) {
        Set<LocalTime> redisTimeSet = new HashSet<>();
        for (LocalTime time : slots) {
            if (redisUtils.hasKey(date + ":" + time)) {
                redisTimeSet.add(time);
            }
        }
        return redisTimeSet;
    }

    // Reservation 에서 시간 추출
    private Set<LocalTime> generateTimeSetFromReservations(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);
        Set<LocalTime> reservationTimeSet = new HashSet<>();
        for (Reservation r : reservations) {
            LocalTime t = r.getStartTime();
            while (t.isBefore(r.getEndTime())) {
                reservationTimeSet.add(t);
                t = t.plusMinutes(30);
            }
        }
        return reservationTimeSet;
    }

    // 전체 시간 목록 생성
    private List<LocalTime> generateSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = OPEN_TIME;
        while (start.isBefore(CLOSE_TIME)) {
            slots.add(start);
            start = start.plusMinutes(30);
        }
        return slots;
    }
}
