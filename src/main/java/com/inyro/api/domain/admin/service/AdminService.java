package com.inyro.api.domain.admin.service;

import com.inyro.api.domain.admin.dto.request.AdminReqDTO;
import com.inyro.api.domain.admin.dto.response.AdminResDTO;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.domain.member.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdminService {
    Page<AdminResDTO.MemberDetailResDTO> getAllUsers(MemberSortType sortType, OrderType order, Pageable pageable);

    void deleteMember(AdminReqDTO.AdminDeleteMemberReqDTO adminDeleteMemberReqDto);

    void changeMemberStatus(Long memberId, Status status);

    AdminResDTO.ReservationsDetailsResDTO getReservations();

    AdminResDTO.ReservationDetailResDTO getReservation(Long reservationId);

    void deleteReservation(AdminReqDTO.AdminDeleteReservationReqDTO adminDeleteReservationReqDTO);
}
