package com.inyro.api.global.security.userdetails.service;

import com.inyro.api.domain.member.entity.Member;
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

    //Username(sno) 로 CustomUserDetail 을 가져오기
    @Override
    public UserDetails loadUserByUsername(String sno) throws UsernameNotFoundException {

        log.info("[ CustomUserDetailsService ] Sno 을 이용하여 User 를 검색합니다.");
        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 학번입니다."));

        Auth auth = member.getAuth();
        if (auth == null) {
            log.error("[ CustomUserDetailsService ] Member에 Auth 정보가 존재하지 않습니다. sno: {}", sno);
            throw new UsernameNotFoundException("인증 정보가 존재하지 않습니다.");
        }

        log.info("[ CustomUserDetailsService ] 사용자 인증 정보 조회 완료 → sno: {}", sno);
        return new CustomUserDetails(member.getSno(), auth.getPassword(), auth.getRole());
    }
}