package com.inyro.api.domain.auth.validator;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AuthReqDto.PasswordChangeReqDTO> {

    @Override
    public boolean isValid(AuthReqDto.PasswordChangeReqDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true; // null인 경우는 다른 검증기가 처리
        return dto.newPassword().equals(dto.newPasswordConfirmation());
    }
}
