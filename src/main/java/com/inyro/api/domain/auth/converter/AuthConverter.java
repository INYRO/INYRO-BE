package com.inyro.api.domain.auth.converter;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.auth.entity.Role;
import com.inyro.api.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    public static Auth toAuth(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO, String password, Member member) {
        return Auth.builder()
                .sno(authSignUpReqDTO.sno())
                .password(password)
                .role(Role.ROLE_USER)
                .member(member)
                .build();
    }
}
