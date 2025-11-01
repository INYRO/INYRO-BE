package com.inyro.api.domain.auth.dto.request;

import com.inyro.api.domain.auth.validator.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.inyro.api.global.constant.MessageConstant.*;
import static com.inyro.api.global.constant.PatternConstant.PASSWORD_PATTERN;
import static com.inyro.api.global.constant.PatternConstant.SNO_PATTERN;

public class AuthReqDTO {


    public record AuthSignUpReqDTO(
            @NotBlank(message = BLANK_SNO)
            @Pattern(regexp = SNO_PATTERN, message = WRONG_SNO)
            String sno,

            @NotBlank(message = BLANK_PASSWORD)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String password,

            @NotBlank(message = BLANK_DEPT)
            String dept,

            @NotBlank(message = BLANK_NAME)
            String name,

            @NotNull(message = BLANK_ENROLLED)
            Boolean enrolled
    ){}

    @Builder
    public record AuthLoginReqDTO(
            @NotBlank(message = BLANK_SNO)
            @Pattern(regexp = SNO_PATTERN, message = WRONG_SNO)
            String sno,

            @NotBlank(message = BLANK_PASSWORD)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String password
    ) {
    }

    @PasswordMatches
    public record PasswordChangeReqDTO(
            @NotBlank(message = BLANK_NEW_PASSWORD)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String newPassword,

            @NotBlank(message = BLANK_PASSWORD_CONFIRMATION)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String newPasswordConfirmation
    ) {
    }

    public record PasswordResetReqDTO(
            @Pattern(regexp = SNO_PATTERN, message = "")
            @NotBlank(message = "학번 입력은 필수입니다.")
            String sno,

            @NotBlank(message = BLANK_NEW_PASSWORD)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String newPassword,

            @NotBlank(message = BLANK_PASSWORD_CONFIRMATION)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String newPasswordConfirmation
    ) {
    }
  
    public record SmulReqDTO(
            @NotBlank(message = BLANK_SNO)
            @Pattern(regexp = SNO_PATTERN, message = WRONG_SNO)
            String sno,

            @NotBlank(message = BLANK_PASSWORD)
            @Pattern(regexp = PASSWORD_PATTERN, message = WRONG_PASSWORD)
            String password
    ) {
    }
}
