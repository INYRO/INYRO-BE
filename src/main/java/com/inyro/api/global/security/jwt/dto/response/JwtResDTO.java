package com.inyro.api.global.security.jwt.dto.response;

import lombok.Builder;

public class JwtResDTO {
    @Builder
    public record JwtATResDTO(
            String accessToken
    ) {}
    @Builder
    public record JwtTokenPairResDTO(
            String accessToken,
            String refreshToken
    ){}
}

