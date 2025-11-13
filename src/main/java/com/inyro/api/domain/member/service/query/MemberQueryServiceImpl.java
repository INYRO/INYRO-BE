package com.inyro.api.domain.member.service.query;

import com.inyro.api.domain.member.MemberReader;
import com.inyro.api.domain.member.converter.MemberConverter;
import com.inyro.api.domain.member.dto.response.MemberResDTO;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberReader memberReader;

    @Override
    public MemberResDTO.MemberDetailResDTO getMemberDetail(String sno) {
        Member member = memberReader.readMember(sno);
        return MemberConverter.toMemberDetailResDto(member);
    }
}
