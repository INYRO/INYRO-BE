package com.inyro.api.domain.reservation.converter;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDTO;
import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.entity.ReservationStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static Reservation toReservation(ReservationReqDTO.ReservationCreateReqDTO reservationCreateReqDTO, LocalTime start, LocalTime end, Member member) {
        return Reservation.builder()
                .participantList(reservationCreateReqDTO.participantList())
                .purpose(reservationCreateReqDTO.purpose())
                .date(reservationCreateReqDTO.date())
                .startTime(start)
                .endTime(end)
                .reservationStatus(ReservationStatus.UPCOMING)
                .member(member)
                .build();
    }

    public static ReservationResDTO.ReservationCreateResDTO toReservationCreateResDTO(Long reservationId, String reservationName, LocalDate date, LocalTime start, LocalTime end) {
        return ReservationResDTO.ReservationCreateResDTO.builder()
                .reservationId(reservationId)
                .reservationName(reservationName)
                .date(String.valueOf(date))
                .startTime(String.valueOf(start))
                .endTime(String.valueOf(end))
                .build();
    }

    public static ReservationResDTO.ReservationAvailableResDTO toReservationAvailableResDTO(LocalDate date, Map<LocalTime, Boolean> availableSlots) {
        return  ReservationResDTO.ReservationAvailableResDTO.builder()
                .date(date)
                .available(availableSlots)
                .build();
    }

    public static ReservationResDTO.ReservationUpdateResDTO toReservationUpdateResDTO(Reservation reservation) {
        return ReservationResDTO.ReservationUpdateResDTO.builder()
                .reservationId(reservation.getId())
                .date(String.valueOf(reservation.getDate()))
                .participantList(reservation.getParticipantList())
                .purpose(reservation.getPurpose())
                .startTime(String.valueOf(reservation.getStartTime()))
                .endTime(String.valueOf(reservation.getEndTime()))
                .build();
    }

    public static ReservationResDTO.ReservationDeleteResDTO toReservationDeleteResDTO(Long reservationId) {
        return ReservationResDTO.ReservationDeleteResDTO.builder()
                .reservationId(reservationId)
                .message("예약이 취소되었습니다.")
                .build();
    }

    public static ReservationResDTO.ReservationDetailResDTO toReservationResDTO(Reservation reservation) {
        return  ReservationResDTO.ReservationDetailResDTO.builder()
                .reservationId(reservation.getId())
                .date(String.valueOf(reservation.getDate()))
                .startTime(String.valueOf(reservation.getStartTime()))
                .endTime(String.valueOf(reservation.getEndTime()))
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }

    public static ReservationResDTO.ReservationTimeResDTO toReservationTimeResDTO(ReservationReqDTO.ReservationTimeReqDTO reservationTimeReqDTO) {
        return ReservationResDTO.ReservationTimeResDTO.builder()
                .date(reservationTimeReqDTO.date())
                .time(reservationTimeReqDTO.time())
                .build();
    }

    public static ReservationResDTO.ReservationTimeReturnResDTO toReservationTimeReturnResDTO(ReservationReqDTO.ReservationTimeReturnReqDTO reservationTimeReturnReqDTO) {
        return ReservationResDTO.ReservationTimeReturnResDTO.builder()
                .date(reservationTimeReturnReqDTO.date())
                .time(reservationTimeReturnReqDTO.time())
                .build();
    }
}
