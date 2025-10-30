package com.inyro.api.domain.admin.service;

import com.inyro.api.domain.admin.converter.AdminConverter;
import com.inyro.api.domain.admin.dto.request.AdminReqDto;
import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.admin.repository.CustomAdminRepository;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final CustomAdminRepository customAdminRepository;

    @Override
    public Page<AdminResDto.MemberDetailResDto> getAllUsers(MemberSortType sortType, OrderType order, Pageable pageable) {

        Page<Member> memberPage = customAdminRepository.findAllMembers(sortType, order, pageable);

        return memberPage.map(AdminConverter::toMemberDetailResDto);
    }

    @Override
    public void deleteMember(AdminReqDto.AdminDeleteMemberReqDto adminDeleteMemberReqDto) {
        memberRepository.deleteAllBySnoIn(adminDeleteMemberReqDto.snoList());
    }
}
