package com.inyro.api.global.security.auth.dto.response;

import lombok.Builder;

public class AuthResponseDto {

    @Builder
    public record PasswordTokenResponseDto(
            String uuid
    ){
    }
}
