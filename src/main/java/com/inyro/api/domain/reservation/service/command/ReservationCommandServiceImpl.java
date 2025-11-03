package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.reservation.converter.ReservationConverter;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDTO;
import com.inyro.api.domain.reservation.dto.response.ReservationResDTO;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.entity.ReservationStatus;
import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import com.inyro.api.domain.reservation.service.lock.ReservationLockService;
import com.inyro.api.domain.reservation.validator.ReservationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationLockService reservationLockService;

    @Override
    public ReservationResDTO.ReservationCreateResDTO createReservation(ReservationReqDTO.ReservationCreateReqDTO reservationCreateReqDTO, String sno) {
        LocalDate date =  reservationCreateReqDTO.date();
        LocalTime start = reservationCreateReqDTO.timeSlots().get(0);
        LocalTime end = reservationCreateReqDTO.timeSlots().get(reservationCreateReqDTO.timeSlots().size() - 1).plusMinutes(30);

        reservationValidator.validateTimeRange(start, end); // 범위 검증
        reservationLockService.validateTimeLock(date, start, end, sno);   // 락 검증

        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Reservation reservation = ReservationConverter.toReservation(reservationCreateReqDTO, start, end, member);
        reservationRepository.save(reservation);
        reservationLockService.deleteTimeLock(date, start, end);   // 락 해제

        return ReservationConverter.toReservationCreateResDTO(reservation.getId(), member.getName(), reservationCreateReqDTO.date(), start, end);
    }

    @Override
    public ReservationResDTO.ReservationUpdateResDTO updateReservation(Long reservationId, ReservationReqDTO.ReservationUpdateReqDTO reservationUpdateReqDTO, String sno) {
        Reservation reservation = reservationRepository.findByIdAndSno(reservationId, sno)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        if (reservationUpdateReqDTO.timeSlots() != null && !reservationUpdateReqDTO.timeSlots().isEmpty()) {
            LocalTime start = reservationUpdateReqDTO.timeSlots().get(0);
            LocalTime end = reservationUpdateReqDTO.timeSlots().get(reservationUpdateReqDTO.timeSlots().size() - 1).plusMinutes(30);
            reservationValidator.validateTimeRange(start, end);

            // 자기 자신 제외한 중복 체크
            if (reservationRepository.existsByDateAndTimeSlotsAndIdNot(reservation.getDate(), start, end, reservation.getId())) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
            }
        }

        reservation.updateReservation(reservationUpdateReqDTO.participantList(), reservationUpdateReqDTO.purpose(), reservationUpdateReqDTO.timeSlots());
        return ReservationConverter.toReservationUpdateResDTO(reservation);
    }

    @Override
    public ReservationResDTO.ReservationDeleteResDTO deleteReservation(Long reservationId, String sno) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        reservation.validateOwner(member.getId());
        reservationRepository.delete(reservation);
        return ReservationConverter.toReservationDeleteResDTO(reservationId);
    }

    @Override
    public void updateExpiredReservations() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        List<Reservation> reservations = reservationRepository.findAllByDateAndStatusBeforeEndTime(date, ReservationStatus.UPCOMING, time);

        if (reservations.isEmpty()) {
            log.info("[ReservationService] 만료된 예약 없음");
            return;
        }
        reservations.forEach(Reservation::completeReservation);
    }

    @Override
    public ReservationResDTO.ReservationTimeResDTO lockTime(String sno, ReservationReqDTO.ReservationTimeReqDTO reservationTimeReqDTO) {
        // 락을 생성하기 전에 이미 예약된 시간에 락을 거는지 확인 (일반적인 사용으로는 불가하나 악의적 접근 방어)
        if (reservationRepository.existsByDateAndTimeSlots(reservationTimeReqDTO.date(), reservationTimeReqDTO.time())) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
        }
        // 해당 날짜+시간에 대한 락 생성
        boolean success = reservationLockService.acquireLock(reservationTimeReqDTO.date(), reservationTimeReqDTO.time(), sno);
        if (!success) {
            // 락이 이미 있는 경우 409
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
        }
        return ReservationConverter.toReservationTimeResDTO(reservationTimeReqDTO);
    }
}
