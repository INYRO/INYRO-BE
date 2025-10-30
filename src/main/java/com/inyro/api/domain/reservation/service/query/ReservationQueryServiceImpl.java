package com.inyro.api.domain.reservation.service.query;

import com.inyro.api.domain.reservation.converter.ReservationConverter;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.service.ReservationCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationCalculator reservationCalculator;

    @Override
    public ReservationResDto.ReservationAvailableResDTO getAvailableReservation(LocalDate date) {
        List<LocalTime> available = reservationCalculator.calculateAvailableSlots(date);
        return ReservationConverter.toReservationAvailableResDTO(date, available);
    }
}
