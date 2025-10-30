package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.member.exception.MemberErrorCode;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.domain.member.repository.MemberRepository;
import com.inyro.api.domain.reservation.converter.ReservationConverter;
import com.inyro.api.domain.reservation.dto.request.ReservationReqDto;
import com.inyro.api.domain.reservation.dto.response.ReservationResDto;
import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.entity.ReservationStatus;
import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.domain.reservation.repository.ReservationRepository;
import com.inyro.api.domain.reservation.validator.ReservationValidator;
import com.inyro.api.global.utils.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final RedisUtils<String, String> redisUtils;

    @Override
    public ReservationResDto.ReservationCreateResDTO createReservation(ReservationReqDto.ReservationCreateReqDTO reservationCreateReqDTO, String sno) {
        LocalDate date =  reservationCreateReqDTO.date();
        LocalTime start = reservationCreateReqDTO.timeSlots().get(0);
        LocalTime end = reservationCreateReqDTO.timeSlots().get(reservationCreateReqDTO.timeSlots().size() - 1).plusMinutes(30);

        reservationValidator.validateTimeRange(start, end); // 범위 검증
        reservationValidator.validateTimeLock(date, start, end, sno);   // 락 검증

        Member member = memberRepository.findBySno(sno)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Reservation reservation = ReservationConverter.toReservation(reservationCreateReqDTO, start, end, member);
        reservationRepository.save(reservation);
        deleteTimeLock(start, end, date);

        return ReservationConverter.toReservationCreateResDTO(reservation.getId(), member.getName(), reservationCreateReqDTO.date(), start, end);
    }

    @Override
    public ReservationResDto.ReservationUpdateResDTO updateReservation(Long reservationId, ReservationReqDto.ReservationUpdateReqDTO reservationUpdateReqDTO, String sno) {
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
    public ReservationResDto.ReservationDeleteResDTO deleteReservation(Long reservationId, String sno) {
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
    public ReservationResDto.ReservationTimeResDto lockTime(String sno, ReservationReqDto.ReservationTimeReqDto reservationTimeReqDTO) {
        if (reservationRepository.existsByDateAndTimeSlots(reservationTimeReqDTO.date(), reservationTimeReqDTO.time())) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
        }
        boolean success = redisUtils.lock(reservationTimeReqDTO.date() + ":" + reservationTimeReqDTO.time(), sno, 300L, TimeUnit.SECONDS);
        if (!success) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
        }
        return ReservationConverter.toReservationTimeResDTO(reservationTimeReqDTO);
    }

    private void deleteTimeLock(LocalTime start, LocalTime end, LocalDate date) {
        while(start.isBefore(end)) {
            redisUtils.delete(date + ":" + start);
            start = start.plusMinutes(30);
        }
    }
}
