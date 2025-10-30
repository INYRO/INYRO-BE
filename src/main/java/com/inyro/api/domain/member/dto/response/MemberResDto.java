package com.inyro.api.domain.member.dto.response;

import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.member.entity.Status;
import lombok.Builder;

import java.util.List;

public class MemberResDto {

    public record AllMemberDetailsResDto(
            List<AdminResDto.UserDetailResDto> userDetails
    ) {
    }

    @Builder
    public record MemberDetailResDto(
            String sno,
            String name,
            String dept,
            Status status
    ) {
    }
}
