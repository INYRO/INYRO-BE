package com.inyro.api.domain.member.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "맴버를 찾을 수 없습니다."),
    DUPLICATE_SNO(HttpStatus.CONFLICT, "MEMBER409", "이미 등록된 학번입니다.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
