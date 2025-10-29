package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.reservation.converter.ReservationConverter;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import com.inyro.api.domain.reservation.validator.ReservationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;

    @Override
    public ReservationResDto.ReservationCreateResDTO createReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, String sno) {
        LocalTime start = reservationCreateReqDTO.timeSlots().get(0);
        LocalTime end = reservationCreateReqDTO.timeSlots().get(reservationCreateReqDTO.timeSlots().size() - 1).plusMinutes(30);
        reservationValidator.validateTimeRange(start, end); // 범위 검증

        if (reservationRepository.existsByDateAndTimeSlots(reservationCreateReqDTO.date(), start, end)){
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
        }

        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Reservation reservation = ReservationConverter.toReservation(reservationCreateReqDTO, start, end, member);
        reservationRepository.save(reservation);
        return ReservationConverter.toReservationCreateResDTO(reservation.getId(), member.getName(), start, end);
    }
}
