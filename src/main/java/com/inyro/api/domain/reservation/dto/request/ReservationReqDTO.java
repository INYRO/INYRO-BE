package com.inyro.api.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.inyro.api.global.constant.MessageConstant.*;

public class ReservationReqDTO {

    public record ReservationCreateReqDTO(
            @NotBlank(message = BLANK_DATE)
            LocalDate date,

            @NotBlank(message = BLANK_PARTICIPANT_LIST)
            String participantList,

            @NotBlank(message = BLANK_PURPOSE)
            String purpose,

            @NotBlank(message = BLANK_TIME_SLOTS)
            List<LocalTime> timeSlots
    ){}

    public record ReservationUpdateReqDTO(
            @NotBlank(message = BLANK_PARTICIPANT_LIST)
            String participantList,

            @NotBlank(message = BLANK_PURPOSE)
            String purpose,

            @NotBlank(message = BLANK_TIME_SLOTS)
            List<LocalTime> timeSlots
    ){}

    public record ReservationTimeReqDTO(
            LocalDate date,
            LocalTime time
    ) {
    }

    public record ReservationTimeReturnReqDTO(
            LocalDate date,
            LocalTime time
    ) {
    }
}
