package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.dto.response.AuthResDto;

public interface AuthCommandService {

    AuthResDto.SmulResDto authenticate(AuthReqDto.SmulReqDto smulReqDto);
}
