package com.inyro.api.domain.reservation.controller;

import com.inyro.api.domain.reservation.dto.request.ReservationReqDTO;
import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;
import com.inyro.api.domain.reservation.service.command.ReservationCommandService;
import com.inyro.api.domain.reservation.service.query.ReservationQueryService;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.apiPayload.PageResponse;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(summary = "예약 생성", description = "락을 획득하지 않은 시간에 대한 예약 불가")
    @PostMapping()
    public CustomResponse<ReservationResDTO.ReservationCreateResDTO> createReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReservationReqDTO.ReservationCreateReqDTO reservationCreateReqDTO
    ) {
        return CustomResponse.onSuccess(reservationCommandService.createReservation(reservationCreateReqDTO, customUserDetails.getUsername()));
    }

    @Operation(summary = "예약 가능한 시간대 조회")
    @GetMapping("/available")
    public CustomResponse<ReservationResDTO.ReservationAvailableResDTO> getAvailableReservations(
            @RequestParam LocalDate date
    ) {
        return CustomResponse.onSuccess(reservationQueryService.getAvailableReservation(date));
    }

    @Operation(summary = "내 예약 조회")
    @GetMapping("/my")
    public CustomResponse<PageResponse<ReservationResDTO.ReservationDetailResDTO>> getMyReservations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return CustomResponse.onSuccess(PageResponse.of(reservationQueryService.getMyReservations(customUserDetails.getUsername(), pageable)));
    }

    @Operation(summary = "예약 수정")
    @PatchMapping("/{reservationId}")
    public CustomResponse<ReservationResDTO.ReservationUpdateResDTO> updateReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("reservationId") Long reservationId,
            @RequestBody ReservationReqDTO.ReservationUpdateReqDTO reservationUpdateReqDTO
    ) {
        return CustomResponse.onSuccess(reservationCommandService.updateReservation(reservationId, reservationUpdateReqDTO, customUserDetails.getUsername()));
    }

    @Operation(summary = "예약 삭제")
    @DeleteMapping("/{reservationId}")
    public CustomResponse<ReservationResDTO.ReservationDeleteResDTO> deleteReservation(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("reservationId") Long reservationId) {
        return CustomResponse.onSuccess(reservationCommandService.deleteReservation(reservationId, customUserDetails.getUsername()));
    }

    @Operation(summary = "시간 점유", description = "단일 시간에 대해서 락을 획득해 예약이 완료될 때까지 또는 5분 동안 접근 제한")
    @PostMapping("/time")
    public CustomResponse<ReservationResDTO.ReservationTimeResDTO> lockTime(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReservationReqDTO.ReservationTimeReqDTO reservationTimeReqDto
    ) {
        return CustomResponse.onSuccess(reservationCommandService.lockTime(customUserDetails.getUsername(), reservationTimeReqDto));
    }

    @Operation(summary = "시간 점유 해제", description = "단일 시간에 대해서 획득한 락을 반납")
    @PostMapping("/time/return")
    public CustomResponse<ReservationResDTO.ReservationTimeReturnResDTO> returnTime(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReservationReqDTO.ReservationTimeReturnReqDTO reservationTimeReturnReqDTO
    ) {
        return CustomResponse.onSuccess(reservationCommandService.returnTime(customUserDetails.getUsername(), reservationTimeReturnReqDTO));
    }
}
