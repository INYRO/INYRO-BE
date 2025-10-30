package com.inyro.api.domain.admin.repository;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAdminRepository {

    Page<Member> findAllMembers(MemberSortType sort, OrderType orderType, Pageable pageable);
}
