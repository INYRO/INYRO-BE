package com.inyro.api.domain.auth.converter;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    public static Auth toAuth(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO, String password, Long memberId) {
        return Auth.builder()
                .sno(authSignUpReqDTO.sno())
                .password(password)
                .build();
    }
}
