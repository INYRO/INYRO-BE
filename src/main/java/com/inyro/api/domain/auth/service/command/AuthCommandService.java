package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;

import com.inyro.api.domain.auth.dto.response.AuthResDto;
import com.inyro.api.global.security.jwt.dto.JwtDto;

public interface AuthCommandService {
    void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO);

    JwtDto reissueToken(JwtDto jwtDto);

    void changePassword(String sno, AuthReqDto.PasswordChangeReqDTO passwordChangeReqDTO);

    void resetPassword(AuthReqDto.PasswordResetReqDTO passwordResetReqDTO);
  
    void resetPasswordByVerification(AuthReqDto.AuthPasswordResetWithCodeReqDTO authPasswordResetWithCodeReqDTO);
  
    AuthResDto.SmulResDto authenticate(AuthReqDto.SmulReqDto smulReqDto);
}
