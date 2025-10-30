package com.inyro.api.domain.admin.service;

import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminResDto.MemberDetailResDto> getAllUsers(MemberSortType sortType, OrderType order, Pageable pageable);
}
