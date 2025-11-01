package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDTO;

import com.inyro.api.domain.auth.dto.response.AuthResDTO;
import com.inyro.api.global.security.jwt.dto.JwtDto;

public interface AuthCommandService {
    void signUp(AuthReqDTO.AuthSignUpReqDTO authSignUpReqDTO);

    JwtDto reissueToken(JwtDto jwtDto);

    void changePassword(String sno, AuthReqDTO.PasswordChangeReqDTO passwordChangeReqDTO);

    void resetPassword(AuthReqDTO.PasswordResetReqDTO passwordResetReqDTO);

    AuthResDTO.SmulResDTO authenticate(AuthReqDTO.SmulReqDTO smulReqDto);
}
