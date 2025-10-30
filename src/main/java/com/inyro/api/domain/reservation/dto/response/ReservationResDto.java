package com.inyro.api.domain.reservation.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class ReservationResDto {

    @Builder
    public record ReservationCreateResDTO(
            Long reservationId,
            String reservationName,
            String date,
            String startTime,
            String endTime
    ){}

    @Builder
    public record ReservationAvailableResDTO (
            LocalDate date,
            List<String> availableSlots
    ){}

    @Builder
    public record ReservationUpdateResDTO (
            Long reservationId,
            String date,
            String participantList,
            String purpose,
            String startTime,
            String endTime
    ){}

    @Builder
    public record ReservationDeleteResDTO (
            Long reservationId,
            String message
    ){}
}
