package com.inyro.api.domain.admin.dto.request;

import java.util.List;

public class AdminReqDto {

    public record AdminDeleteMemberReqDto(
            List<String> snoList
    ) {
    }
}
