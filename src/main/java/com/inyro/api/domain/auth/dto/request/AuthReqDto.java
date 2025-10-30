package com.inyro.api.domain.auth.dto.request;

import com.inyro.api.domain.auth.validator.PasswordMatches;
import com.inyro.api.global.security.utils.PasswordPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class AuthReqDto {


    public record AuthSignUpReqDTO(
            @NotBlank
            String sno,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String password,
            @NotBlank
            String dept,
            @NotBlank
            String name,
            Boolean enrolled
    ){}

    @Builder
    public record AuthLoginReqDTO(
            @NotBlank(message = "학번 입력은 필수입니다.")
            String sno,
            @NotBlank(message = "비밀번호 입력은 필수입니다.")
            String password
    ) {
    }

    @PasswordMatches
    public record PasswordChangeReqDTO(
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPassword,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPasswordConfirmation
    ) {
    }

    public record PasswordResetReqDTO(
            @NotBlank(message = "학번 입력은 필수입니다.")
            String sno,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPassword,
            @NotBlank
            @Pattern(regexp = PasswordPattern.REGEXP, message = PasswordPattern.MESSAGE)
            String newPasswordConfirmation
    ) {
    }
  
    public record SmulReqDto(
            String sno,
            String password
    ) {
    }
}
