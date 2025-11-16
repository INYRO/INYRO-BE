package com.inyro.api.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CookieUtils {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenExpiration;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    /**
     * Refresh Token 쿠키 생성
     * 환경별로 보안 설정 자동 조정
     */
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);  // XSS 방어
        cookie.setPath("/");
        cookie.setMaxAge((int) refreshTokenExpiration/ 1000);

        boolean isLocal = "local".equals(activeProfile);

        if (isLocal) {
            // 로컬 개발 환경: HTTP 프론트 + HTTPS 백엔드
            log.info("[ Cookie Util ] 로컬 환경 - SameSite=None, Secure=true");
            cookie.setSecure(true);  // SameSite=None은 Secure 필수
            cookie.setAttribute("SameSite", "None");
        } else {
            // 운영 환경: 엄격한 보안
            log.info("[ Cookie Util ] 운영 환경 - SameSite=Strict, Secure=true");
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "Strict");
        }

        return cookie;
    }

    /**
     * Response에 Refresh Token 쿠키 추가
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);
        log.info("[ Cookie Util ] Refresh Token 쿠키가 추가되었습니다.");
    }

    /**
     * Refresh Token 쿠키 삭제 (로그아웃 시 사용)
     */
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 삭제

        boolean isLocal = "local".equals(activeProfile);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", isLocal ? "None" : "Strict");

        response.addCookie(cookie);
        log.info("[ Cookie Util ] Refresh Token 쿠키가 삭제되었습니다.");
    }
}
