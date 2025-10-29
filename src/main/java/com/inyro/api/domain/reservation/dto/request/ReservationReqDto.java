package com.inyro.api.domain.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationReqDto {

    public record ReservationCreateReqDTO(
            LocalDate date,
            String participantList,
            String purpose,
            List<LocalTime> timeSlots
    ){}
}
