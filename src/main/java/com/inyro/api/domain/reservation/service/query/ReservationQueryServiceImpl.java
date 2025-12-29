package com.inyro.api.domain.reservation.service.query;

import com.inyro.api.domain.reservation.converter.ReservationConverter;
import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import com.inyro.api.domain.reservation.service.ReservationCalculator;
import com.inyro.api.domain.reservation.validator.ReservationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationCalculator reservationCalculator;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;

    @Override
    public ReservationResDTO.ReservationAvailableResDTO getAvailableReservation(LocalDate date) {
        // 이미 지난 날짜인지 검증
        reservationValidator.validateNotPasteDate(date);
        Map<LocalTime, Boolean> available = reservationCalculator.calculateAvailableSlots(date);
        return ReservationConverter.toReservationAvailableResDTO(date, available);
    }

    @Override
    public Page<ReservationResDTO.ReservationDetailResDTO> getMyReservations(String sno, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAllBySno(sno, pageable);
        return reservations.map(ReservationConverter::toReservationResDTO);
    }
}
