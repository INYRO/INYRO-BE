package com.inyro.api.domain.reservation.service.query;

import com.inyro.api.domain.reservation.dto.response.ReservationResDto;

import java.time.LocalDate;

public interface ReservationQueryService {
    ReservationResDto.ReservationAvailableResDTO getAvailableReservation(LocalDate date);
}
