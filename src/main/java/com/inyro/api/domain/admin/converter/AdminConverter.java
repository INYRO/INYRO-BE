package com.inyro.api.domain.admin.converter;

import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminConverter {

    public static AdminResDto.MemberDetailResDto toMemberDetailResDto(Member member) {
        return AdminResDto.MemberDetailResDto.builder()
                .name(member.getName())
                .sno(member.getSno())
                .dept(member.getDept())
                .status(member.getStatus())
                .build();
    }
}
