package com.inyro.api.domain.reservation.converter;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationConverter {

    public static Reservation toReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, LocalTime start, LocalTime end, Member member) {
        return Reservation.builder()
                .participantList(reservationCreateReqDTO.participantList())
                .purpose(reservationCreateReqDTO.purpose())
                .date(reservationCreateReqDTO.date())
                .startTime(start)
                .endTime(end)
                .member(member)
                .build();
    }

    public static ReservationResDto.ReservationCreateResDTO toReservationCreateResDTO(Long reservationId, String reservationName, LocalTime start, LocalTime end) {
        return ReservationResDto.ReservationCreateResDTO.builder()
                .reservationId(reservationId)
                .reservationName(reservationName)
                .startTime(start)
                .endTime(end)
                .build();
    }
}
