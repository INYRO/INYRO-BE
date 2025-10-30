package com.inyro.api.domain.auth.converter;


import com.inyro.api.domain.auth.dto.request.AuthReqDto;
import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.auth.entity.Role;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.auth.dto.response.AuthResDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {


    public static Auth toAuth(AuthReqDto.AuthSignUpReqDTO authSignUpReqDTO, String password, Member member) {
        return Auth.builder()
                .sno(authSignUpReqDTO.sno())
                .password(password)
                .role(Role.ROLE_USER)
                .member(member)
                .build();
    }

    public static AuthResDto.SmulResDto toSmulResDto(AuthResDto.ClubInfo clubInfo, AuthResDto.DeptInfo deptInfo) {
        return AuthResDto.SmulResDto.builder()
                .sno(deptInfo.STDNO())
                .name(deptInfo.NM_KOR())
                .dept(deptInfo.TMP_DEPT_MJR_NM())
                .registered(isRegistered(clubInfo.STUD_APLY_YN()))
                .build();
    }

    private static boolean isRegistered(String YN){
        return YN.equals("Y");
    }
}
