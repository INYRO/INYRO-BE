package com.inyro.api.domain.reservation.converter;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.entity.ReservationStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static Reservation toReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, LocalTime start, LocalTime end, Member member) {
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

    public static ReservationResDto.ReservationCreateResDTO toReservationCreateResDTO(Long reservationId, String reservationName, LocalDate date, LocalTime start, LocalTime end) {
        return ReservationResDto.ReservationCreateResDTO.builder()
                .reservationId(reservationId)
                .reservationName(reservationName)
                .date(String.valueOf(date))
                .startTime(String.valueOf(start))
                .endTime(String.valueOf(end))
                .build();
    }

    public static ReservationResDto.ReservationAvailableResDTO toReservationAvailableResDTO(LocalDate date, List<LocalTime> availableSlots) {
        List<String> formatted = availableSlots.stream()
                .map(t -> t.format(TIME_FORMATTER))
                .toList();
        return  ReservationResDto.ReservationAvailableResDTO.builder()
                .date(date)
                .availableSlots(formatted)
                .build();
    }

    public static ReservationResDto.ReservationUpdateResDTO toReservationUpdateResDTO(Reservation reservation) {
        return ReservationResDto.ReservationUpdateResDTO.builder()
                .reservationId(reservation.getId())
                .date(String.valueOf(reservation.getDate()))
                .participantList(reservation.getParticipantList())
                .purpose(reservation.getPurpose())
                .startTime(String.valueOf(reservation.getStartTime()))
                .endTime(String.valueOf(reservation.getEndTime()))
                .build();
    }

    public static ReservationResDto.ReservationDeleteResDTO toReservationDeleteResDTO(Long reservationId) {
        return ReservationResDto.ReservationDeleteResDTO.builder()
                .reservationId(reservationId)
                .message("예약이 취소되었습니다.")
                .build();
    }

    public static ReservationResDto.ReservationResDTO toReservationResDTO(Reservation reservation) {
        return  ReservationResDto.ReservationResDTO.builder()
                .reservationId(reservation.getId())
                .date(String.valueOf(reservation.getDate()))
                .startTime(String.valueOf(reservation.getStartTime()))
                .endTime(String.valueOf(reservation.getEndTime()))
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
