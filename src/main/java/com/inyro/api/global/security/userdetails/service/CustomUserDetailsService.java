package com.inyro.api.global.security.userdetails.service;

import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.domain.auth.repository.AuthRepository;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.auth.entity.Auth;

import com.inyro.api.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    //Username(sno) 로 CustomUserDetail 을 가져오기
    @Override
    public UserDetails loadUserByUsername(String sno) throws UsernameNotFoundException {

        log.info("[ CustomUserDetailsService ] Sno 을 이용하여 User 를 검색합니다.");
        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        log.info("[ CustomUserDetailsService ] Member 를 이용하여 Auth 를 검색합니다.");
        Auth auth = authRepository.findByMember(member)
                .orElseThrow(() -> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));
//        Optional<Member> memberEntity = memberRepository.findByEmailAndNotDeleted(email);
//        if (memberEntity.isPresent()) {
//            Member member = memberEntity.get();
//            return new CustomUserDetails(member.getEmail(),member.getPassword(), member.getRole());
//        }
//        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        return new CustomUserDetails(member.getSno(), auth.getPassword(), auth.getRole());
    }
}