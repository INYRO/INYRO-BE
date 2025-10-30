package com.inyro.api.domain.admin.controller;

import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.admin.service.AdminService;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin 관련 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "유저 목록 조회")
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public CustomResponse<PageResponse<AdminResDto.MemberDetailResDto>> getAllUsers(
            @RequestParam(required = false) MemberSortType sortType,
            @RequestParam(required = false) OrderType order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return CustomResponse.onSuccess(PageResponse.of(adminService.getAllUsers(sortType, order, pageable)));
    }
}
