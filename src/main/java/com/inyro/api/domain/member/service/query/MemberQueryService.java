package com.inyro.api.domain.member.service.query;

import com.inyro.api.domain.member.dto.response.MemberResDto;

public interface MemberQueryService {

    MemberResDto.MemberDetailResDto getMemberDetail(String sno);
}
