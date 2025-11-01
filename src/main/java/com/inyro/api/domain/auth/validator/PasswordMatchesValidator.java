package com.inyro.api.domain.auth.validator;

import com.inyro.api.domain.auth.dto.request.AuthReqDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AuthReqDTO.PasswordChangeReqDTO> {

    @Override
    public boolean isValid(AuthReqDTO.PasswordChangeReqDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true; // null인 경우는 다른 검증기가 처리
        return dto.newPassword().equals(dto.newPasswordConfirmation());
    }
}
