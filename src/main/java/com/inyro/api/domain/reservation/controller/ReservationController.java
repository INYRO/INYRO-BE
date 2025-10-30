package com.inyro.api.domain.reservation.controller;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.service.command.ReservationCommandService;
import com.inyro.api.domain.reservation.service.query.ReservationQueryService;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.apiPayload.PageResponse;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "내 예약 조회")
    @GetMapping("/my")
    public CustomResponse<PageResponse<ReservationResDto.ReservationResDTO>> getMyReservations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return CustomResponse.onSuccess(PageResponse.of(reservationQueryService.getMyReservations(customUserDetails.getUsername(), pageable)));
    }

    @Operation(summary = "예약 수정")
    @PatchMapping("/{reservationId}")
    public CustomResponse<ReservationResDto.ReservationUpdateResDTO> updateReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("reservationId") Long reservationId,
            @RequestBody ReservationReqDto.ReservationUpdateReqDTO reservationUpdateReqDTO
            ){
        return CustomResponse.onSuccess(reservationCommandService.updateReservation(reservationId, reservationUpdateReqDTO, customUserDetails.getUsername()));
    }

    @Operation(summary = "예약 삭제")
    @DeleteMapping("/{reservationId}")
    public CustomResponse<ReservationResDto.ReservationDeleteResDTO> deleteReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("reservationId") Long reservationId){
        return CustomResponse.onSuccess(reservationCommandService.deleteReservation(reservationId, customUserDetails.getUsername()));
    }
}
