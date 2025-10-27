package com.inyro.api.global.apiPayload.exception;

import com.inyro.api.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

  private final BaseErrorCode code;

  public CustomException(BaseErrorCode errorCode) {
    this.code = errorCode;
  }
}