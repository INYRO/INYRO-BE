package com.inyro.api.global.security.auth.controller;

import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.security.auth.dto.request.AuthRequestDto;
import com.inyro.api.global.security.auth.service.AuthService;
import com.inyro.api.global.security.jwt.dto.JwtDto;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

    private final AuthService authService;

    //토큰 재발급 API
    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(
            @RequestBody JwtDto jwtDto
    ) throws SignatureException {

        log.info("[ Auth Controller ] 토큰을 재발급합니다. ");

        return CustomResponse.onSuccess(authService.reissueToken(jwtDto));
    }

    @Operation(summary = "login", description = "이메일과 비밀번호로 로그인을 해 jwt 토큰을 받는다.")
    @PostMapping("/login")
    public CustomResponse<?> login(
            @RequestBody AuthRequestDto.LoginRequestDto memberLoginRequestDto
    ) {
        return null;
    }

    @Operation(summary = "logout", description = "현재 jwt 토큰을 블랙리스트로 등록해 사용 불가하게 만든다.")
    @PostMapping("/logout")
    public CustomResponse<?> logout() {
        return null;
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호와 바꿀 비밀번호를 입력해 비밀번호를 변경한다.")
    @PostMapping("/password/reset")
    public CustomResponse<String> resetPassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid AuthRequestDto.PasswordResetRequestDto passwordResetRequestDto
    ) {
        authService.resetPassword(customUserDetails.getUsername(), passwordResetRequestDto);
        return CustomResponse.onSuccess(HttpStatus.OK, "비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "비밀번호 재설정 (잃어버렸으르 때)", description = "메일 인증 코드 확인으로 발급된 토큰")
    @PostMapping("/password/reset/code")
    public CustomResponse<String> resetPasswordWithCode(
            @RequestHeader("PasswordToken") String passwordTokenHeader,
            @RequestBody AuthRequestDto.PasswordResetWithCodeRequestDto passwordResetWithCodeRequestDto
    ) {
        authService.resetPasswordWithCode(passwordTokenHeader, passwordResetWithCodeRequestDto);
        return CustomResponse.onSuccess(HttpStatus.OK, "비밀번호 변경이 완료되었습니다.");
    }

}
