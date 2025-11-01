package com.inyro.api.domain.auth.dto.response;

import lombok.Builder;

import java.util.List;

public class AuthResDTO {

    @Builder
    public record SmulResDTO(
            String sno,
            String name,
            String dept,
            boolean registered
    ) {
    }

    @Builder
    public record ClubInfoDTO(
            List<ClubInfo> dsClubAplyList
    ) {
    }

    public record ClubInfo(
            String INDPT_ORG_NM,  // 동아리 이름
            String STUD_APLY_YN,  // 가입 여부 (Y/N)
            String WKDTY_NM,      // 역할 이름 (예: 회원)
            String WKDTY_CD       // 역할 코드 (예: USD003.0003)
    ) {
    }

    public record DeptInfoDTO(
            List<DeptInfo> dsStdInfoList
    ) {
    }
    @Builder
    public record DeptInfo(
            String STDNO,         // 학번
            String NM_KOR,          // 이름
            String TMP_DEPT_MJR_NM  // 전공
    ){
    }
}
