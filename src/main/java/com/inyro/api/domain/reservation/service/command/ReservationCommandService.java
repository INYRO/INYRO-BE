package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;

public interface ReservationCommandService {
    ReservationResDto.ReservationCreateResDTO createReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, String sno);
}
