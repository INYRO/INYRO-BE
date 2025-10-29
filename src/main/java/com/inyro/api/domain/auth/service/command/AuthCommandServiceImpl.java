package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.converter.AuthConverter;
import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.service.command.MemberCommandService;
import com.inyro.api.global.security.jwt.JwtUtil;
import com.inyro.api.global.security.jwt.dto.JwtDto;
import com.inyro.api.global.security.jwt.entity.Token;
import com.inyro.api.global.security.jwt.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberCommandService memberCommandService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO) {

        Member member = memberCommandService.createMember(authSignUpReqDTO.name(), authSignUpReqDTO.sno(), authSignUpReqDTO.major());

        String password = passwordEncoder.encode(authSignUpReqDTO.password());
        Auth auth = AuthConverter.toAuth(authSignUpReqDTO, password, member);

        member.linkAuth(auth);
    }

    @Override
    public JwtDto reissueToken(JwtDto tokenDto) {
        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
        String accessToken = tokenDto.accessToken();
        String refreshToken = tokenDto.refreshToken();

        //Access Token 으로부터 사용자 Sno 추출
        String sno = jwtUtil.getSno(refreshToken); // **수정부분**
        log.info("[ Auth Service ] Sno ---> {}", sno);

        //Access Token 에서의 Email 로 부터 DB 에 저장된 Refresh Token 가져오기
        Token refreshTokenByDB = tokenRepository.findBySno(sno).orElseThrow(
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
    public void resetPassword(String sno, AuthReqDto.PasswordResetRequestDto passwordResetRequestDto) {

//        if (!passwordResetRequestDto.newPassword().equals(passwordResetRequestDto.newPasswordConfirmation())) {
//            throw new AuthException(AuthErrorCode.NEW_PASSWORD_DOES_NOT_MATCH);
//        }
//
//        Member member = memberRepository.findByEmail(sno)
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
    public void resetPasswordWithCode(String passwordTokenHeader, AuthReqDto.PasswordResetWithCodeRequestDto passwordResetWithCodeRequestDto) {
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
