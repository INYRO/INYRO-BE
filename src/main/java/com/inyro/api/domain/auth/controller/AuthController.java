package com.inyro.api.domain.auth.controller;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;

import com.inyro.api.domain.auth.service.command.AuthCommandService;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.security.jwt.dto.JwtDto;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import com.inyro.api.domain.auth.dto.response.AuthResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;


    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public CustomResponse<String> signUp(
            @Valid @RequestBody AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO
    ) {
        authCommandService.signUp(authSignUpReqDTO);
        return CustomResponse.onSuccess("회원가입 성공");
    }

    //토큰 재발급 API
    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(@RequestBody JwtDto jwtDto) {
        log.info("[ Auth Controller ] 토큰을 재발급합니다. ");
        return CustomResponse.onSuccess(authCommandService.reissueToken(jwtDto));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public CustomResponse<?> login(@RequestBody AuthReqDto.AuthLoginReqDTO authLoginReqDTO) {
        throw new UnsupportedOperationException("이 API는 Swagger 문서용입니다.");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public CustomResponse<?> logout() {
        throw new UnsupportedOperationException("이 API는 Swagger 문서용입니다.");
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호와 바꿀 비밀번호를 입력해 비밀번호를 변경한다.")
    @PostMapping("/password/reset")
    public CustomResponse<String> changePassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody AuthReqDto.PasswordChangeReqDTO authPasswordResetReqDTO
    ) {
        authCommandService.changePassword(customUserDetails.getUsername(), authPasswordResetReqDTO);
        return CustomResponse.onSuccess("비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "비밀번호 재설정 (잃어버렸으르 때)", description = "메일 인증 코드 확인으로 발급된 토큰")
    @PostMapping("/password/reset/smul")
    public CustomResponse<String> resetPassword(
            @RequestBody AuthReqDto.PasswordResetReqDTO passwordResetReqDTO
    ) {
        authCommandService.resetPassword(passwordResetReqDTO);
        return CustomResponse.onSuccess(HttpStatus.OK, "비밀번호 변경이 완료되었습니다.");
    }

    @Operation(summary = "샘물 인증", description = "사용자로부터 샘물 아이디 비밀번호를 입력받아 동아리 회원인지 인증")
    @PostMapping("/smul")
    public CustomResponse<AuthResDto.SmulResDto> authenticate(AuthReqDto.SmulReqDto smulReqDto) {
        return CustomResponse.onSuccess(authCommandService.authenticate(smulReqDto));
    }
}
