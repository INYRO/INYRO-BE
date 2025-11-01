package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDTO;
import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;

public interface ReservationCommandService {
    ReservationResDTO.ReservationCreateResDTO createReservation(ReservationReqDTO.ReservationCreateReqDTO reservationCreateReqDTO, String sno);

    ReservationResDTO.ReservationUpdateResDTO updateReservation(Long reservationId, ReservationReqDTO.ReservationUpdateReqDTO reservationUpdateReqDTO, String sno);

    ReservationResDTO.ReservationDeleteResDTO deleteReservation(Long reservationId, String sno);

    void updateExpiredReservations();

    ReservationResDTO.ReservationTimeResDTO lockTime(String sno, ReservationReqDTO.ReservationTimeReqDTO reservationTimeReqDTO);
}
