package com.inyro.api.domain.reservation.service;

import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ReservationCalculator {

    private final ReservationRepository reservationRepository;

    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);

    public List<LocalTime> calculateAvailableSlots(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);
        List<LocalTime> allSlots = generateSlots(OPEN_TIME, CLOSE_TIME);

        Set<LocalTime> reserved = new HashSet<>();
        for (Reservation r : reservations) {
            LocalTime t = r.getStartTime();
            while (t.isBefore(r.getEndTime())) {
                reserved.add(t);
                t = t.plusMinutes(30);
            }
        }

        return allSlots.stream()
                .filter(slot -> !reserved.contains(slot))
                .toList();
    }

    private List<LocalTime> generateSlots(LocalTime start, LocalTime end) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = start;
        while (current.isBefore(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }
        return slots;
    }
}
