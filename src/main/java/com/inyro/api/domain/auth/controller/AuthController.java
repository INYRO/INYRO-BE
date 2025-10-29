package com.inyro.api.domain.auth.controller;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.dto.response.AuthResDto;
import com.inyro.api.domain.auth.service.command.AuthCommandService;
import com.inyro.api.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "샘물 인증", description = "사용자로부터 샘물 아이디 비밀번호를 입력받아 동아리 회원인지 인증")
    @PostMapping()
    public CustomResponse<AuthResDto.SmulResDto> authenticate(AuthReqDto.SmulReqDto smulReqDto) {
        return CustomResponse.onSuccess(authCommandService.authenticate(smulReqDto));
    }
}
