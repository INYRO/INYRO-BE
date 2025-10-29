package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.global.security.jwt.dto.JwtDto;

public interface AuthCommandService {
    void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO);

    JwtDto reissueToken(JwtDto jwtDto);

    void resetPassword(String sno, AuthReqDto.AuthPasswordResetReqDTO authPasswordResetReqDTO);

    void resetPasswordWithCode(String passwordTokenHeader, AuthReqDto.PasswordResetWithCodeRequestDto passwordResetWithCodeRequestDto);
}
