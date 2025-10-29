package com.inyro.api.domain.auth.service.command;

import com.inyro.api.domain.auth.converter.AuthConverter;
import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.dto.response.AuthResDto;
import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private static final String LOGIN_URL = "https://smsso.smu.ac.kr/Login.do";
    private static final String BASE_URL = "https://smul.smu.ac.kr";

    private final WebClient clubWebClient;

    @Override
    public AuthResDto.SmulResDto authenticate(AuthReqDto.SmulReqDto smulReqDto) {
        AuthResDto.ClubInfo clubInfo = getClubData(smulReqDto);
        AuthResDto.DeptInfo deptInfo = getDept(smulReqDto);
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
                throw new AuthException(AuthErrorCode.AUTH_UNAUTHORIZED);
            return Jsoup.connect(BASE_URL.concat("/index.do"))
                    .method(Connection.Method.GET)
                    .cookies(loginResponse.cookies())
                    .execute()
                    .cookies();
        } catch (IOException e) {
            throw new AuthException(AuthErrorCode.AUTH_INTERNAL_SERVER_ERROR);
        }
    }

    private AuthResDto.ClubInfo getClubData(AuthReqDto.SmulReqDto smulReqDto) {

        Map<String, String> cookies = login(smulReqDto);

        String cookieHeader = cookies.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("; "));

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
            throw new AuthException(AuthErrorCode.AUTH_INTERNAL_SERVER_ERROR);
        }
        return responseBody.dsClubAplyList().stream()
                .filter(info -> info.INDPT_ORG_NM().equals("이니로(INYRO)"))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorCode.NO_CLUB_INFO));
    }

    private AuthResDto.DeptInfo getDept(AuthReqDto.SmulReqDto smulReqDto) {
        Map<String, String> cookies = login(smulReqDto);
        String cookieHeader = cookies.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("; "));
        AuthResDto.DeptInfoDto responseBody = clubWebClient.post()
                .uri("/UsrSchMng/selectStdInfo.do")
                .header("Cookie", cookieHeader)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData("_AUTH_MENU_KEY", "usrCPsnlInfoUpd-STD")
//                        .with("@d1#strCampusRcd", "CMN001.0001")
//                        .with("@d1#strSchYear", String.valueOf(LocalDate.now().getYear()))
                        .with("@d1#strStdNo", smulReqDto.sno())
                        .with("@d#", "@d1#")
                        .with("@d1#", "dmParam")
                        .with("@d1#tp", "dm")
                )
                .retrieve()
                .bodyToMono(AuthResDto.DeptInfoDto.class)
                .block();
        return responseBody.dsStdInfoList().get(0);
    }
}
