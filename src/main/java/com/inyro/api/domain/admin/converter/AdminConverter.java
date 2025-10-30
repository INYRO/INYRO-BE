package com.inyro.api.domain.admin.converter;

import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminConverter {

    public static AdminResDto.MemberDetailResDto toMemberDetailResDto(Member member) {
        return AdminResDto.MemberDetailResDto.builder()
                .name(member.getName())
                .sno(member.getSno())
                .dept(member.getDept())
                .status(member.getStatus())
                .build();
    }

    public static AdminResDto.ReservationDetailResDto toReservationDetailResDto(Reservation reservation) {
        return AdminResDto.ReservationDetailResDto.builder()
                .memberId(reservation.getMember().getId())
                .reservationId(reservation.getId())
                .participantList(reservation.getParticipantList())
                .purpose(reservation.getPurpose())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    public static AdminResDto.ReservationsDetailsResDto toReservationsDetailsResDto(List<AdminResDto.ReservationDetailResDto> reservationDetailResDtoList) {
        return AdminResDto.ReservationsDetailsResDto.builder()
                .reservations(reservationDetailResDtoList)
                .build();
    }
}
