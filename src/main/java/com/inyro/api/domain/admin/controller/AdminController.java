package com.inyro.api.domain.admin.controller;

import com.inyro.api.domain.admin.dto.request.AdminReqDTO;
import com.inyro.api.domain.admin.dto.response.AdminResDTO;
import com.inyro.api.domain.admin.service.AdminService;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.domain.member.entity.Status;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin 관련 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 멤버 목록 조회")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public CustomResponse<PageResponse<AdminResDTO.MemberDetailResDTO>> getAllUsers(
            @RequestParam(required = false) MemberSortType sortType,
            @RequestParam(required = false) OrderType order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return CustomResponse.onSuccess(PageResponse.of(adminService.getAllUsers(sortType, order, pageable)));
    }

    @Operation(summary = "관리자 멤버 삭제")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/members/withdrawal")
    public CustomResponse<String> deleteMember(
            @RequestBody AdminReqDTO.AdminDeleteMemberReqDTO adminDeleteMemberReqDto
    ) {
        adminService.deleteMember(adminDeleteMemberReqDto);
        return CustomResponse.onSuccess(HttpStatus.NO_CONTENT, "유저 삭제 완료");
    }

    @Operation(summary = "관리자 멤버 속성 변경")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/members/{memberId}/status")
    public CustomResponse<String> changeMemberStatus(
            @PathVariable Long memberId,
            @RequestParam Status status
    ) {
        adminService.changeMemberStatus(memberId, status);
        return CustomResponse.onSuccess("유저 status 변경 완료");
    }

    @Operation(summary = "관리자 예약 목록 조회")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservations")
    public CustomResponse<AdminResDTO.ReservationsDetailsResDTO> getReservations() {
        return CustomResponse.onSuccess(adminService.getReservations());
    }

    @Operation(summary = "관리자 예약 단일 조회")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservations/{reservationId}")
    public CustomResponse<AdminResDTO.ReservationDetailResDTO> getReservation(
            @PathVariable Long reservationId
    ) {
        return CustomResponse.onSuccess(adminService.getReservation(reservationId));
    }

    @Operation(summary = "관리자 예약 삭제")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reservations")
    public CustomResponse<String> deleteReservation(
            @RequestBody AdminReqDTO.AdminDeleteReservationReqDTO adminDeleteReservationReqDTO
    ) {
        adminService.deleteReservation(adminDeleteReservationReqDTO);
        return CustomResponse.onSuccess(HttpStatus.NO_CONTENT, "예약 삭제 완료");
    }
}
