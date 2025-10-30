package com.inyro.api.domain.admin.service;

import com.inyro.api.domain.admin.converter.AdminConverter;
import com.inyro.api.domain.admin.dto.request.AdminReqDto;
import com.inyro.api.domain.admin.dto.response.AdminResDto;
import com.inyro.api.domain.admin.repository.CustomAdminRepository;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.entity.MemberSortType;
import com.inyro.api.domain.member.entity.OrderType;
import com.inyro.api.domain.member.entity.Status;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final CustomAdminRepository customAdminRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Page<AdminResDto.MemberDetailResDto> getAllUsers(MemberSortType sortType, OrderType order, Pageable pageable) {

        Page<Member> memberPage = customAdminRepository.findAllMembers(sortType, order, pageable);

        return memberPage.map(AdminConverter::toMemberDetailResDto);
    }

    @Override
    public void deleteMember(AdminReqDto.AdminDeleteMemberReqDto adminDeleteMemberReqDto) {
        memberRepository.deleteAllBySnoIn(adminDeleteMemberReqDto.snoList());
    }

    @Override
    public void changeMemberStatus(long memberId, Status status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.changeStatus(status);
    }

    @Override
    public AdminResDto.ReservationsDetailsResDto getReservations() {
        List<Reservation> reservations = reservationRepository.findAllByOrderByIdDesc();

        List<AdminResDto.ReservationDetailResDto> reservationDetailResDtoList = reservations.stream()
                .map(AdminConverter::toReservationDetailResDto)
                .toList();

        return AdminConverter.toReservationsDetailsResDto(reservationDetailResDtoList);
    }

    @Override
    public AdminResDto.ReservationDetailResDto getReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        return AdminConverter.toReservationDetailResDto(reservation);
    }
}
