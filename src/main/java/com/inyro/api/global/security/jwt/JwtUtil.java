package com.inyro.api.global.security.jwt;

import com.inyro.api.domain.auth.entity.Role;
import com.inyro.api.global.security.jwt.dto.response.JwtResDTO;
import com.inyro.api.global.security.jwt.entity.Token;
import com.inyro.api.global.security.jwt.repository.TokenRepository;
import com.inyro.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final TokenRepository tokenRepository;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token.access-expiration-time}") Long access,
            @Value("${spring.jwt.token.refresh-expiration-time}") Long refresh,
            TokenRepository tokenRepo
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMs = access;
        this.refreshExpMs = refresh;
        this.tokenRepository = tokenRepo;
    }

    // 학번(sno) 추출
    public String getSno(String token) throws SignatureException {
        log.info("[JwtUtil] 토큰에서 학번을 추출합니다.");
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 권한(Role) 추출
    public Role getRoles(String token) throws SignatureException {
        log.info("[JwtUtil] 토큰에서 권한을 추출합니다.");
        String roleStr = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        return Role.valueOf(roleStr);
    }

    // 토큰 생성 공통 메서드
    private String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {
        log.info("[JwtUtil] 토큰을 새로 생성합니다.");
        Instant issuedAt = Instant.now();

        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(customUserDetails.getUsername())
                .claim("role", authorities)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    // Access Token 발급
    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(accessExpMs);
        return tokenProvider(customUserDetails, expiration);
    }

    // Refresh Token 발급
    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshExpMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);

        tokenRepository.save(Token.builder()
                .sno(customUserDetails.getUsername())
                .token(refreshToken)
                .build());

        return refreshToken;
    }

    // Refresh Token 기반 재발급
    public JwtResDTO.JwtTokenPairResDTO reissueToken(String refreshToken) throws SignatureException {
        CustomUserDetails userDetails = new CustomUserDetails(
                getSno(refreshToken),
                null,
                getRoles(refreshToken)
        );
        log.info("[JwtUtil] 새로운 토큰을 재발급합니다.");

        return new JwtResDTO.JwtTokenPairResDTO(
                createJwtAccessToken(userDetails),
                createJwtRefreshToken(userDetails)
        );
    }

    // Authorization 헤더에서 JWT 추출
    public String resolveAccessToken(HttpServletRequest request) {
        log.info("[JwtUtil] 헤더에서 토큰을 추출합니다.");
        String tokenFromHeader = request.getHeader("Authorization");

        if (tokenFromHeader == null || !tokenFromHeader.startsWith("Bearer ")) {
            log.warn("[JwtUtil] Request Header 에 토큰이 존재하지 않습니다.");
            return null;
        }

        return tokenFromHeader.substring(7);
    }

    // 토큰 유효성 검증
    public void validateToken(String token) {
        log.info("[JwtUtil] 토큰의 유효성을 검증합니다.");
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new SecurityException("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        }
    }

    // 남은 유효 시간 (로그아웃용)
    public long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }
}
