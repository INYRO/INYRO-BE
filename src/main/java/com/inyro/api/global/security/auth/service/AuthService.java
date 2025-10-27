package com.inyro.api.global.security.auth.service;

import com.inyro.api.global.security.auth.dto.request.AuthRequestDto;
import com.inyro.api.global.security.jwt.dto.JwtDto;

public interface AuthService {
    JwtDto reissueToken(JwtDto jwtDto);

    void resetPassword(String email, AuthRequestDto.PasswordResetRequestDto passwordResetRequestDto);

    void resetPasswordWithCode(String passwordTokenHeader, AuthRequestDto.PasswordResetWithCodeRequestDto passwordResetWithCodeRequestDto);
}
