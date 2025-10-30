package com.inyro.api.domain.member.dto.response;

import com.inyro.api.domain.member.entity.Status;
import lombok.Builder;


public class MemberResDto {

    @Builder
    public record MemberDetailResDto(
            long id,
            String sno,
            String name,
            String dept,
            Status status
    ) {
    }
}
