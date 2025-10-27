package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;

public interface AuthCommandService {
    void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO);
}
