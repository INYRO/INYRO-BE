package com.inyro.api.domain.reservation.service.query;

import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReservationQueryService {
    ReservationResDTO.ReservationAvailableResDTO getAvailableReservation(LocalDate date);

    Page<ReservationResDTO.ReservationDetailResDTO> getMyReservations(String sno, Pageable pageable);
}
