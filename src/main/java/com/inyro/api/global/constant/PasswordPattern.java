package com.inyro.api.global.constant;


public class PasswordPattern {
    public static final String REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}|:;<>,.?~])[A-Za-z\\d!@#$%^&*()_+=\\-{}|:;<>,.?~]{8,}$";
    public static final String MESSAGE = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.";

    private PasswordPattern() {}

}
