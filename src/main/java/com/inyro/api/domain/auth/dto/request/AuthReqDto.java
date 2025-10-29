package com.inyro.api.domain.auth.dto.request;

public class AuthReqDto {

    public record SmulReqDto(
            String sno,
            String password
    ) {
    }
}
