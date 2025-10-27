package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.converter.AuthConverter;
import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.service.command.MemberCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberCommandService memberCommandService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO) {

        Member member = memberCommandService.createMember(authSignUpReqDTO.name(), authSignUpReqDTO.sno(), authSignUpReqDTO.major());

        String password = passwordEncoder.encode(authSignUpReqDTO.password());
        Auth auth = AuthConverter.toAuth(authSignUpReqDTO, password, member.getId());

        member.linkAuth(auth);
    }
}
