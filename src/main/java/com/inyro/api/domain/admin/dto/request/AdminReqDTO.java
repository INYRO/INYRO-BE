package com.inyro.api.domain.admin.dto.request;

import java.util.List;

public class AdminReqDTO {

    public record AdminDeleteMemberReqDTO(
            List<String> snoList
    ) {
    }
}
