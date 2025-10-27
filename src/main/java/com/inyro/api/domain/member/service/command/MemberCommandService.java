package com.inyro.api.domain.member.service.command;

import com.inyro.api.domain.member.entity.Member;

public interface MemberCommandService {
    Member createMember(String name, String sno, String major);
}
