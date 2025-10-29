package com.inyro.api.domain.auth.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다"),
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "COMMON401", "이상한 토큰입니다"),
    NEW_PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "PASS400_1", "새 비밀번호와 비밀번호 재입력이 일치하지 않습니다."),
    CURRENT_PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "PASS400_2", "현재 비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_IS_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "PASS400_3", "현재 비빌번호와 새 비밀번호가 일치합니다."),
    SMUL_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH500", "샘물 오류"),
    SMUL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH403", "샘물 아이디 또는 비밀번호가 틀림"),
    NO_CLUB_INFO(HttpStatus.NOT_FOUND, "AUTH404", "이니로 동아리 정보가 존재하지 않음"),
    SMUL_VALIDATION_DOES_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "AUTH422", "샘물 인증을 시도하지 않았거나 변조되었거나 만료되었습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
