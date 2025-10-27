package com.inyro.api.global.security.jwt.dto;

import lombok.Builder;

@Builder
public record JwtDto(
    String accessToken,
    String refreshToken
) {
}
