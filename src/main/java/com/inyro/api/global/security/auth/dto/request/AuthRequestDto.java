package com.inyro.api.global.security.auth.dto.request;

import com.inyro.api.global.security.utils.PasswordPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class AuthRequestDto {

    @Builder
    public record LoginRequestDto(
            String email,
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
