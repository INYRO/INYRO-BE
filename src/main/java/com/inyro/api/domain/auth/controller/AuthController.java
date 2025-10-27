package com.inyro.api.domain.auth.controller;

import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.service.command.AuthCommandService;
import com.inyro.api.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth 관련 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @PostMapping("/signup")
    public CustomResponse<String> signUp(@RequestBody AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO) {
        authCommandService.signUp(authSignUpReqDTO);
        return CustomResponse.onSuccess("회원가입 성공");
    }


}
