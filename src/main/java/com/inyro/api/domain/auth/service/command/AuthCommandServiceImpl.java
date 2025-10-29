package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.converter.AuthConverter;
import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.member.service.command.MemberCommandService;
import com.inyro.api.global.security.jwt.JwtUtil;
import com.inyro.api.global.security.jwt.dto.JwtDto;
import com.inyro.api.global.security.jwt.entity.Token;
import com.inyro.api.global.security.jwt.repository.TokenRepository;
import com.inyro.api.global.utils.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.inyro.api.domain.auth.dto.response.AuthResDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberCommandService memberCommandService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final WebClient clubWebClient;
    private final RedisUtils<String> redisUtils;

    private static final String LOGIN_URL = "https://smsso.smu.ac.kr/Login.do";
    private static final String BASE_URL = "https://smul.smu.ac.kr";

    @Override
    public void signUp(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO) {
        if (!redisUtils.hasKey(authSignUpReqDTO.sno())) {
            throw new AuthException(AuthErrorCode.SMUL_VALIDATION_DOES_NOT_EXIST);
        }

        if (memberRepository.findBySno(authSignUpReqDTO.sno()).isPresent()) {
            throw new MemberException(MemberErrorCode.DUPLICATE_SNO);
        }
        Member member = memberCommandService.createMember(authSignUpReqDTO.name(), authSignUpReqDTO.sno(), authSignUpReqDTO.major());

        String encodedPassword = passwordEncoder.encode(authSignUpReqDTO.password());
        Auth auth = AuthConverter.toAuth(authSignUpReqDTO, encodedPassword, member);

        member.linkAuth(auth);

        redisUtils.delete(authSignUpReqDTO.sno());
    }

    @Override
    public JwtDto reissueToken(JwtDto tokenDto) {
        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
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
    public void resetPassword(String sno, AuthReqDto.AuthPasswordResetReqDTO authPasswordResetReqDTO) {
        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Auth auth = member.getAuth();
        auth.validateNotSamePassword(authPasswordResetReqDTO.newPassword(), passwordEncoder);
        auth.resetPassword(passwordEncoder.encode(authPasswordResetReqDTO.newPassword()));
    }

    @Override
    public void resetPasswordWithCode(String passwordTokenHeader, AuthReqDto.AuthPasswordResetWithCodeReqDTO passwordResetWithCodeRequestDto) {
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
    @Override
    public AuthResDto.SmulResDto authenticate(AuthReqDto.SmulReqDto smulReqDto) {
        String cookieHeader = getCookieHeader(smulReqDto);
        AuthResDto.ClubInfo clubInfo = getClubData(smulReqDto, cookieHeader);
        if (clubInfo.STUD_APLY_YN().equals("Y")) {
            redisUtils.save(smulReqDto.sno(), clubInfo.STUD_APLY_YN(), 300L, TimeUnit.MINUTES);
        }
        AuthResDto.DeptInfo deptInfo = getDeptData(smulReqDto, cookieHeader);
        return AuthConverter.toSmulResDto(clubInfo, deptInfo);
    }

    private Map<String, String> login(AuthReqDto.SmulReqDto smulReqDto) {
        try {
            Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
                    .method(Connection.Method.POST)
                    .data("user_id", smulReqDto.sno())
                    .data("user_password", smulReqDto.password())
                    .execute();
            if (loginResponse.url().toString().equals(LOGIN_URL))
                throw new AuthException(AuthErrorCode.SMUL_UNAUTHORIZED);
            return Jsoup.connect(BASE_URL.concat("/index.do"))
                    .method(Connection.Method.GET)
                    .cookies(loginResponse.cookies())
                    .execute()
                    .cookies();
        } catch (IOException e) {
            throw new AuthException(AuthErrorCode.SMUL_INTERNAL_SERVER_ERROR);
        }
    }

    private AuthResDto.ClubInfo getClubData(AuthReqDto.SmulReqDto smulReqDto, String cookieHeader) {
        AuthResDto.ClubInfoDto responseBody = clubWebClient.post()
                .uri("/UsdMembReg/list.do")
                .header("Cookie", cookieHeader)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData("_AUTH_MENU_KEY", "usdCMembReg-STD")
                        .with("@d1#strCampusRcd", "CMN001.0001")
                        .with("@d1#strSchYear", String.valueOf(LocalDate.now().getYear()))
                        .with("@d1#strStdNo", smulReqDto.sno())
                        .with("@d#", "@d1#")
                        .with("@d1#tp", "dm"))
                .retrieve()
                .bodyToMono(AuthResDto.ClubInfoDto.class)
                .block();
        if (responseBody == null) {
            throw new AuthException(AuthErrorCode.SMUL_INTERNAL_SERVER_ERROR);
        }
        return responseBody.dsClubAplyList().stream()
                .filter(info -> info.INDPT_ORG_NM().equals("이니로(INYRO)"))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorCode.NO_CLUB_INFO));
    }

    private AuthResDto.DeptInfo getDeptData(AuthReqDto.SmulReqDto smulReqDto, String cookieHeader) {
        AuthResDto.DeptInfoDto responseBody = clubWebClient.post()
                .uri("/UsrSchMng/selectStdInfo.do")
                .header("Cookie", cookieHeader)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData("_AUTH_MENU_KEY", "usrCPsnlInfoUpd-STD")
                        .with("@d1#strStdNo", smulReqDto.sno())
                        .with("@d#", "@d1#")
                        .with("@d1#", "dmParam")
                        .with("@d1#tp", "dm")
                )
                .retrieve()
                .bodyToMono(AuthResDto.DeptInfoDto.class)
                .block();
        if (responseBody == null) {
            throw new AuthException(AuthErrorCode.SMUL_INTERNAL_SERVER_ERROR);
        }
        return responseBody.dsStdInfoList().get(0);
    }

    private String getCookieHeader(AuthReqDto.SmulReqDto smulReqDto) {
        Map<String, String> cookies = login(smulReqDto);
        return cookies.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("; "));
    }
}
