package com.inyro.api.domain.reservation.dto.response;

import com.inyro.api.domain.reservation.entity.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class ReservationResDTO {

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
            Map<LocalTime, Boolean> available
    ){}

    @Builder
    public record ReservationDetailResDTO(
            Long reservationId,
            String date,
            String startTime,
            String endTime,
            ReservationStatus reservationStatus
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

    @Builder
    public record ReservationTimeResDTO(
            LocalDate date,
            LocalTime time
    ) {
    }

    @Builder
    public record ReservationTimeReturnResDTO(
            LocalDate date,
            LocalTime time
    ) {
    }
}
