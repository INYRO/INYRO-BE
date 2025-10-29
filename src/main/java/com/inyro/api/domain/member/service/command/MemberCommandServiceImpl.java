package com.inyro.api.domain.member.service.command;

import com.inyro.api.domain.member.converter.MemberConverter;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;

    @Override
    public Member createMember(String name, String sno, String major) {
        Member member = MemberConverter.toMember(name, sno, major);
        return memberRepository.save(member);
    }
}
