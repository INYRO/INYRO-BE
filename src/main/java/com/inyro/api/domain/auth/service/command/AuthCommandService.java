package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDTO;

import com.inyro.api.domain.auth.dto.response.AuthResDTO;
import com.inyro.api.global.security.jwt.dto.response.JwtResDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthCommandService {
    void signUp(AuthReqDTO.AuthSignUpReqDTO authSignUpReqDTO);

    JwtResDTO.JwtATResDTO reissueToken(String refreshToken, HttpServletResponse response);

    void changePassword(String sno, AuthReqDTO.PasswordChangeReqDTO passwordChangeReqDTO);

    void resetPassword(AuthReqDTO.PasswordResetReqDTO passwordResetReqDTO);

    AuthResDTO.SmulResDTO authenticate(AuthReqDTO.SmulReqDTO smulReqDto);
}
