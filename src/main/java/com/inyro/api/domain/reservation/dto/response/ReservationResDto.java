package com.inyro.api.domain.reservation.dto.response;

import lombok.Builder;

import java.time.LocalTime;

public class ReservationResDto {

    @Builder
    public record ReservationCreateResDTO(
            Long reservationId,
            String reservationName,
            String startTime,
            String endTime
    ){}
}
