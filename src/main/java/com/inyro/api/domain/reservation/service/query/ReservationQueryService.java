package com.inyro.api.domain.reservation.service.query;

import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReservationQueryService {
    ReservationResDto.ReservationAvailableResDTO getAvailableReservation(LocalDate date);

    Page<ReservationResDto.ReservationResDTO> getMyReservations(String sno, Pageable pageable);
}
