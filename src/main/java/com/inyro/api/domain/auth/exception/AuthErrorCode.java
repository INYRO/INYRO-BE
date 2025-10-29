package com.inyro.api.domain.auth.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    // ErrorCode
    AUTH_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH500", "샘물 오류"),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH403", "샘물 아이디 또는 비밀번호가 틀림"),
    NO_CLUB_INFO(HttpStatus.NOT_FOUND, "AUTH404", "이니로 동아리 정보가 존재하지 않음"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
