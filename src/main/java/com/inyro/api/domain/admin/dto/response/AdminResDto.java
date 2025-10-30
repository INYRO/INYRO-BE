package com.inyro.api.domain.admin.dto.response;

import com.inyro.api.domain.member.entity.Status;
import lombok.Builder;

public class AdminResDto {

    @Builder
    public record MemberDetailResDto(
            String sno,
            String name,
            String dept,
            Status status
    ) {
    }
}
