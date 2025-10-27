package com.inyro.api.domain.auth.dto.request;

import com.inyro.api.global.security.utils.PasswordPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class AuthReqDto {

    public record AuthSignUpReqDTO(
            String sno,
            String password,
            String major,
            String name,
            Boolean enrolled
    ){}

    @Builder
    public record LoginRequestDto(
            String sno,
            String password
    ) {
    }

    public record PasswordResetRequestDto(
            @NotBlank
            String currentPassword,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPassword,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPasswordConfirmation
    ) {
    }

    public record PasswordResetWithCodeRequestDto(
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPassword,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPasswordConfirmation
    ) {
    }
}
