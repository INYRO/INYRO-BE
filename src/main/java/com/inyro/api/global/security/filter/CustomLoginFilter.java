package com.inyro.api.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.inyro.api.domain.auth.dto.request.AuthReqDTO;
import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.global.apiPayload.CustomResponse;
import com.inyro.api.global.security.jwt.JwtUtil;
import com.inyro.api.global.security.jwt.dto.response.JwtResDTO;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import com.inyro.api.global.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Validator validator;
    private final CookieUtil cookieUtil;

    //로그인 시도 메서드
    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws AuthenticationException {

        log.info("[ Login Filter ]  로그인 시도 : Custom Login Filter 작동 ");
        ObjectMapper objectMapper = new ObjectMapper();
        AuthReqDTO.AuthLoginReqDTO requestBody;
        try {
            requestBody = objectMapper.readValue(request.getInputStream(), AuthReqDTO.AuthLoginReqDTO.class);
        } catch (IOException e) {
            throw new AuthException(AuthErrorCode.AUTH_NOT_FOUND);
        }

        //DTO Validation 체크
        Set<ConstraintViolation<AuthReqDTO.AuthLoginReqDTO>> violations = validator.validate(requestBody);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new BadCredentialsException(errorMessage);
        }

        //Request Body 에서 추출
        String sno = requestBody.sno(); //Sno 추출
        String password = requestBody.password(); //password 추출
        log.info("[ Login Filter ]  Sno ---> {} ", sno);
        log.info("[ Login Filter ]  Password ---> {} ", password);

        //UserNamePasswordToken 생성 (인증용 객체)
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(sno, password, null);


        log.info("[ Login Filter ] 인증용 객체 UsernamePasswordAuthenticationToken 이 생성되었습니다. ");
        log.info("[ Login Filter ] 인증을 시도합니다.");

        //인증 시도
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시
    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authentication) throws IOException {


        log.info("[ Login Filter ] 로그인에 성공 하였습니다.");

        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

        String accessToken = jwtUtil.createJwtAccessToken(customUserDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(customUserDetails);

        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        //Client 에게 줄 Response 를 Build
        JwtResDTO.JwtATResDTO jwtDto = JwtResDTO.JwtATResDTO.builder()
                .accessToken(accessToken) //access token 생성
                .build();

        // CustomResponse 사용하여 응답 통일
        CustomResponse<JwtResDTO.JwtATResDTO> responseBody = CustomResponse.onSuccess(jwtDto);

        //JSON 변환
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value()); //Response 의 ReservationStatus 를 200으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        //Body 에 토큰이 담긴 Response 쓰기
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
    @Override
    protected void unsuccessfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException failed) throws IOException {

        log.info("[ Login Filter ] 로그인에 실패하였습니다.");

        String errorCode;
        String errorMessage;

        if (failed instanceof BadCredentialsException) {
            String message = failed.getMessage();

            // Validation 에러와 인증 실패 구분
            if (message != null && message.contains("필수")) {
                errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
                errorMessage = message;
            } else {
                errorCode = String.valueOf(HttpStatus.UNAUTHORIZED.value());
                errorMessage = "학번 혹은 비밀번호가 잘못되었습니다.";
            }
        } else if (failed instanceof LockedException) {
            errorCode = String.valueOf(HttpStatus.LOCKED.value());
            errorMessage = "계정이 잠금 상태입니다.";
        } else if (failed instanceof DisabledException) {
            errorCode = String.valueOf(HttpStatus.FORBIDDEN.value());
            errorMessage = "계정이 비활성화 되었습니다.";
        } else if (failed instanceof UsernameNotFoundException) {
            errorCode = String.valueOf(HttpStatus.NOT_FOUND.value());
            errorMessage = "계정을 찾을 수 없습니다.";
        } else if (failed instanceof AuthenticationServiceException) {
            errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
            errorMessage = "Request Body 파싱 중 오류가 발생했습니다.";
        } else {
            errorCode = String.valueOf(HttpStatus.UNAUTHORIZED.value());
            errorMessage = "인증에 실패했습니다.";
        }

        // CustomResponse 사용하여 응답 통일
        CustomResponse<JwtResDTO.JwtATResDTO> responseBody = CustomResponse.onFailure(errorCode, errorMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(Integer.parseInt(errorCode)); // HTTP 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody)); // 응답 변환 및 출력
    }
}
