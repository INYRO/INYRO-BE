package com.inyro.api.domain.admin.dto.response;

import com.inyro.api.domain.member.entity.Status;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminResDTO {

    @Builder
    public record MemberDetailResDTO(
            String sno,
            String name,
            String dept,
            Status status
    ) {
    }

    @Builder
    public record ReservationsDetailsResDTO(
            List<ReservationDetailResDTO> reservations
    ) {
    }

    @Builder
    public record ReservationDetailResDTO(
            long memberId,
            long reservationId,
            String participantList,
            String purpose,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
    }
}
