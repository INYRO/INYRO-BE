package com.inyro.api.domain.member.converter;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.entity.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {

    public static Member toMember(String name, String sno, String major){
        return Member.builder()
                .name(name)
                .sno(sno)
                .major(major)
                .status(Status.ENROLLED)
                .auth(null)
                .build();
    }
}
