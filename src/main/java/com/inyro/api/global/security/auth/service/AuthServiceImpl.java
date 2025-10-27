package com.inyro.api.global.security.auth.service;

import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.domain.auth.repository.AuthRepository;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.global.security.auth.dto.request.AuthRequestDto;
import com.inyro.api.global.security.auth.entity.Auth;
import com.inyro.api.global.security.jwt.JwtUtil;
import com.inyro.api.global.security.jwt.dto.JwtDto;
import com.inyro.api.global.security.jwt.entity.Token;
import com.inyro.api.global.security.jwt.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    @Override
    public JwtDto reissueToken(JwtDto tokenDto) {
        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
        String accessToken = tokenDto.accessToken();
        String refreshToken = tokenDto.refreshToken();

        //Access Token 으로부터 사용자 Email 추출
        String email = jwtUtil.getEmail(refreshToken); // **수정부분**
        log.info("[ Auth Service ] Email ---> {}", email);

        //Access Token 에서의 Email 로 부터 DB 에 저장된 Refresh Token 가져오기
        Token refreshTokenByDB = tokenRepository.findByEmail(email).orElseThrow(
                () -> new AuthException(AuthErrorCode.INVALID_TOKEN)
        );

        //Refresh Token 이 유효한지 검사
        jwtUtil.validateToken(refreshToken);

        log.info("[ Auth Service ] Refresh Token 이 유효합니다.");

        //만약 DB 에서 찾은 Refresh Token 과 파라미터로 온 Refresh Token 이 일치하면 새로운 토큰 발급
        if (refreshTokenByDB.getToken().equals(refreshToken)) {
            log.info("[ Auth Service ] 토큰을 재발급합니다.");
            return jwtUtil.reissueToken(refreshToken);
        } else {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public void resetPassword(String email, AuthRequestDto.PasswordResetRequestDto passwordResetRequestDto) {

//        if (!passwordResetRequestDto.newPassword().equals(passwordResetRequestDto.newPasswordConfirmation())) {
//            throw new AuthException(AuthErrorCode.NEW_PASSWORD_DOES_NOT_MATCH);
//        }
//
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        Auth auth = member.getAuth();
//
//        if (!passwordEncoder.matches(passwordResetRequestDto.currentPassword(), auth.getPassword())) {
//            throw new AuthException(AuthErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH);
//        }
//        if (passwordEncoder.matches(passwordResetRequestDto.newPassword(), auth.getPassword())) {
//            throw new AuthException(AuthErrorCode.NEW_PASSWORD_IS_CURRENT_PASSWORD);
//        }
//
//        auth.updatePassword(passwordEncoder.encode(passwordResetRequestDto.newPassword()));
//        authRepository.save(auth);

    }

    @Override
    public void resetPasswordWithCode(String passwordTokenHeader, AuthRequestDto.PasswordResetWithCodeRequestDto passwordResetWithCodeRequestDto) {
//        final String uuid = passwordTokenHeader.replace("PasswordToken ", "").trim();
//        log.info("헤더다 : {}", passwordTokenHeader);
//        final String redisKey = "password_token : " + uuid;
//
//        final String email = redisTemplate.opsForValue().get(redisKey);
//
//        if (email == null) {
//            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
//        }
//
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        Auth auth = member.getAuth();
//
//        if (!passwordResetWithCodeRequestDto.newPassword().equals(passwordResetWithCodeRequestDto.newPasswordConfirmation())) {
//            throw new AuthException(AuthErrorCode.NEW_PASSWORD_DOES_NOT_MATCH);
//        }
//
//        auth.updatePassword(passwordEncoder.encode(passwordResetWithCodeRequestDto.newPassword()));
//        authRepository.save(auth);
//
//        mailService.sendPasswordChangeNotification(email);
    }
}
