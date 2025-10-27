package com.inyro.api.domain.auth.dto.request;

public class AuthReqDto {

    public record AuthSignUpReqDTO(
            String sno,
            String password,
            String major,
            String name,
            Boolean enrolled
    ){}
}
