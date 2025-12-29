package com.inyro.api.domain.admin.converter;

import com.inyro.api.domain.admin.dto.response.AdminResDTO;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminConverter {

    public static AdminResDTO.MemberDetailResDTO toMemberDetailResDto(Member member) {
        return AdminResDTO.MemberDetailResDTO.builder()
                .name(member.getName())
                .sno(member.getSno())
                .dept(member.getDept())
                .status(member.getStatus())
                .build();
    }

    public static AdminResDTO.ReservationDetailResDTO toReservationDetailResDto(Reservation reservation) {
        return AdminResDTO.ReservationDetailResDTO.builder()
                .memberId(reservation.getMember().getId())
                .reservationId(reservation.getId())
                .participantList(reservation.getParticipantList())
                .purpose(reservation.getPurpose())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    public static AdminResDTO.ReservationSummaryDTO toReservationSummaryResDto(Reservation reservation) {
        return AdminResDTO.ReservationSummaryDTO.builder()
                .reservationId(reservation.getId())
                .name(reservation.getMember().getName())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    public static AdminResDTO.ReservationsDetailsResDTO toReservationsDetailsResDto(List<AdminResDTO.ReservationSummaryDTO> reservationDetailResDtoList) {
        return AdminResDTO.ReservationsDetailsResDTO.builder()
                .reservations(reservationDetailResDtoList)
                .build();
    }
}
