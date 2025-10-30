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

    public Map<String, Boolean> calculateAvailableSlots(LocalDate date) {

        List<String> allSlots = generateSlots();
        Set<String> reservationTimeSet = generateTimeSetFromReservations(date);
        Set<String> redisTimeSet = generateTimeSetFromRedis(date.toString(), allSlots);
        // TimeSet 병합
        Set<String> mergedTimeSet = new HashSet<>(reservationTimeSet);
        mergedTimeSet.addAll(redisTimeSet);

        // 순서가 보장되는 맵으로 예약 가능한 시간을 표시
        Map<String, Boolean> availableMap = new LinkedHashMap<>();
        for (String time : allSlots) {
            availableMap.put(time, !mergedTimeSet.contains(time));
        }
        return availableMap;
    }

    // Redis 락에서 시간 추출
    private Set<String> generateTimeSetFromRedis(String date, List<String> slots) {
        Set<String> redisTimeSet = new HashSet<>();
        for (String time : slots) {
            if (redisUtils.hasKey(date + ":" + time)) {
                redisTimeSet.add(time);
            }
        }
        return redisTimeSet;
    }

    // Reservation 에서 시간 추출
    private Set<String> generateTimeSetFromReservations(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);
        Set<String> reservationTimeSet = new HashSet<>();
        for (Reservation r : reservations) {
            LocalTime t = r.getStartTime();
            while (t.isBefore(r.getEndTime())) {
                reservationTimeSet.add(t.toString());
                t = t.plusMinutes(30);
            }
        }
        return reservationTimeSet;
    }

    // 전체 시간 목록 생성
    private List<String> generateSlots() {
        List<String> slots = new ArrayList<>();
        LocalTime start = OPEN_TIME;
        while (start.isBefore(CLOSE_TIME)) {
            slots.add(start.toString());
            start = start.plusMinutes(30);
        }
        return slots;
    }
}
