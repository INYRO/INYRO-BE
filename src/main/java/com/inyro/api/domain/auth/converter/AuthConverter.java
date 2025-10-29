package com.inyro.api.domain.auth.converter;

import com.inyro.api.domain.auth.dto.response.AuthResDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {

    public static AuthResDto.SmulResDto toSmulResDto(AuthResDto.ClubInfo clubInfo, AuthResDto.DeptInfo deptInfo) {
        return AuthResDto.SmulResDto.builder()
                .sno(clubInfo.STDNO())
                .dept(deptInfo.TMP_DEPT_MJR_NM())
                .registered(isRegistered(clubInfo.STUD_APLY_YN()))
                .build();
    }

    private static boolean isRegistered(String YN){
        return YN.equals("Y");
    }
}
