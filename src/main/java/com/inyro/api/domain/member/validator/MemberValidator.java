package com.inyro.api.domain.member.validator;

import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateDuplicateSno(String sno) {
        if (memberRepository.findBySno(sno).isPresent()) {
            throw new MemberException(MemberErrorCode.DUPLICATE_SNO);
        }
    }
}
