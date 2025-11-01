package com.inyro.api.domain.member.service.query;

import com.inyro.api.domain.member.dto.response.MemberResDTO;

public interface MemberQueryService {

    MemberResDTO.MemberDetailResDTO getMemberDetail(String sno);
}
