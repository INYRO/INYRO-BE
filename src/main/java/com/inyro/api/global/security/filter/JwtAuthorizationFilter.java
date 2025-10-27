package com.inyro.api.global.security.filter;

import com.inyro.api.domain.auth.entity.Role;
import com.inyro.api.global.security.jwt.JwtUtil;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // JWT 관련 유틸리티 클래스 주입
    private final JwtUtil jwtUtil;

    // redis 주입
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("[ JwtAuthorizationFilter ] 인가 필터 작동");

        try {
            // 1. Request에서 Access Token 추출
            String accessToken = jwtUtil.resolveAccessToken(request);

            // 2. Access Token이 없으면 다음 필터로 바로 진행
            if (accessToken == null) {
                log.info("[ JwtAuthorizationFilter ] Access Token 없음, 다음 필터로 진행");
                filterChain.doFilter(request, response);
                return;
            }

            log.info("[ JwtAuthorizationFilter ] 로그아웃 여부 확인");
            String isLogout = redisTemplate.opsForValue().get("Logout " + accessToken);
            if (isLogout != null) {
//                throw new AuthException(AuthErrorCode.BLACKLISTED_TOKEN);
                log.info("[ JwtAuthorizationFilter ] 블랙리스트 토큰. 인증 생략하고 다음 필터로 진행");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Access Token을 이용한 인증 처리
            authenticateAccessToken(accessToken);
            log.info("[ JwtAuthorizationFilter ] 종료. 다음 필터로 넘어갑니다.");

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // 4. 토큰 만료 시 401 응답 처리
            logger.warn("[ JwtAuthorizationFilter ] accessToken 이 만료되었습니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access Token 이 만료되었습니다.");
        }
    }

    // Access Token을 바탕으로 인증 객체 생성 및 SecurityContext에 저장
    private void authenticateAccessToken(String accessToken) {
        log.info("[ JwtAuthorizationFilter ] 토큰으로 인가 과정을 시작합니다. ");

        // 1. Access Token의 유효성 검증
        jwtUtil.validateToken(accessToken);
        log.info("[ JwtAuthorizationFilter ] Access Token 유효성 검증 성공. ");

        // 2. Access Token에서 사용자 정보 추출 후 CustomUserDetails 생성
        String email = jwtUtil.getEmail(accessToken);
        Role role = jwtUtil.getRoles(accessToken);
        log.info("[ JwtAuthorizationFilter ] email = {}, role = {}", email, role);

        CustomUserDetails userDetails = new CustomUserDetails(email, "", role);

        log.info("[ JwtAuthorizationFilter ] UserDetails 객체 생성 성공");

        // 3. 인증 객체 생성 및 SecurityContextHolder에 저장
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        log.info("[ JwtAuthorizationFilter ] 인증 객체 생성 완료");

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("[ JwtAuthorizationFilter ] 인증 객체 저장 완료");


    }
}