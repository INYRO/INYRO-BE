package com.inyro.api.domain.admin.service;

import com.inyro.api.domain.admin.dto.request.AdminReqDto;
import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.domain.member.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminResDto.MemberDetailResDto> getAllUsers(MemberSortType sortType, OrderType order, Pageable pageable);

    void deleteMember(AdminReqDto.AdminDeleteMemberReqDto adminDeleteMemberReqDto);

    void changeMemberStatus(long memberId, Status status);

    AdminResDto.ReservationsDetailsResDto getReservations();

    AdminResDto.ReservationDetailResDto getReservation(long reservationId);

    void deleteReservation(long reservationId);
}
