package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;

public interface ReservationCommandService {
    ReservationResDto.ReservationCreateResDTO createReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, String sno);

    ReservationResDto.ReservationUpdateResDTO updateReservation(Long reservationId, ReservationReqDto.ReservationUpdateReqDTO reservationUpdateReqDTO, String sno);

    ReservationResDto.ReservationDeleteResDTO deleteReservation(Long reservationId, String sno);
}
