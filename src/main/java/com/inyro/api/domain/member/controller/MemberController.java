package com.inyro.api.domain.member.controller;

import com.inyro.api.domain.member.dto.response.MemberResDto;
import com.inyro.api.domain.member.service.command.MemberCommandService;
import com.inyro.api.domain.member.service.query.MemberQueryService;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/my")
    public CustomResponse<MemberResDto.MemberDetailResDto> getMemberDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return CustomResponse.onSuccess(memberQueryService.getMemberDetail(customUserDetails.getUsername()));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping()
    public CustomResponse<String> deleteMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        memberCommandService.deleteMember(customUserDetails.getUsername());
        return CustomResponse.onSuccess("회원 정보가 삭제 되었습니다.");
    }
}
