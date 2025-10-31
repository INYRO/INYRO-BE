package com.inyro.api.domain.member.converter;

import com.inyro.api.domain.member.dto.response.MemberResDTO;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.entity.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {

    public static Member toMember(String name, String sno, String dept){
        return Member.builder()
                .name(name)
                .sno(sno)
                .dept(dept)
                .status(Status.ENROLLED)
                .auth(null)
                .build();
    }

    public static MemberResDTO.MemberDetailResDTO toMemberDetailResDto(Member member){
        return MemberResDTO.MemberDetailResDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .sno(member.getSno())
                .dept(member.getDept())
                .status(member.getStatus())
                .build();
    }
}
