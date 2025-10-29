package com.inyro.api.domain.reservation.controller;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.service.command.ReservationCommandService;
import com.inyro.api.domain.reservation.service.query.ReservationQueryService;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation", description = "Reservation 관련 API")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @Operation(summary = "예약 생성")
    @PostMapping()
    public CustomResponse<ReservationResDto.ReservationCreateResDTO> createReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO
            ) {
        return CustomResponse.onSuccess(reservationCommandService.createReservation(reservationCreateReqDTO, customUserDetails.getUsername()));
    }

    @Operation(summary = "예약 가능한 시간대 조회")
    @GetMapping("/available")
    public CustomResponse<ReservationResDto.ReservationAvailableResDTO> getAvailableReservations(@RequestParam LocalDate date){
        return CustomResponse.onSuccess(reservationQueryService.getAvailableReservation(date));
    }
}
