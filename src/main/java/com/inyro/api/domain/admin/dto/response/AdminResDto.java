package com.inyro.api.domain.admin.dto.response;

import com.inyro.api.domain.member.entity.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminResDto {

    @Builder
    public record MemberDetailResDto(
            String sno,
            String name,
            String dept,
            Status status
    ) {
    }

    @Builder
    public record ReservationsDetailsResDto(
            List<ReservationDetailResDto> reservations
    ) {
    }

    @Builder
    public record ReservationDetailResDto(
            long memberId,
            long reservationId,
            String people,
            String purpose,
            LocalDate date,
            List<LocalTime> time
    ) {
    }
}
