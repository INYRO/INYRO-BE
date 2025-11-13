package com.inyro.api.domain.reservation.service.command;

import com.inyro.api.domain.member.MemberReader;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.ReservationReader;
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

    private final MemberReader memberReader;
    private final ReservationReader reservationReader;
    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationLockService reservationLockService;

    public record TimeRange(LocalTime start, LocalTime end) {}

    @Override
    public ReservationResDTO.ReservationCreateResDTO createReservation(ReservationReqDTO.ReservationCreateReqDTO reservationCreateReqDTO, String sno) {
        LocalDate date =  reservationCreateReqDTO.date();
        TimeRange range = calculateTimeRange(reservationCreateReqDTO.timeSlots());

        reservationValidator.validateTimeRange(range.start, range.end); // 범위 검증
        reservationLockService.validateTimeLock(date, range.start, range.end, sno);   // 락 검증

        try{
            Member member = memberReader.readMember(sno);
            Reservation reservation = ReservationConverter.toReservation(reservationCreateReqDTO, range.start, range.end, member);
            reservationRepository.save(reservation);
            return ReservationConverter.toReservationCreateResDTO(reservation.getId(), member.getName(), reservationCreateReqDTO.date(), range.start, range.end);
        } finally {
            reservationLockService.deleteTimeLock(date, range.start, range.end);   // 락 해제
        }
    }

    @Override
    public ReservationResDTO.ReservationUpdateResDTO updateReservation(Long reservationId, ReservationReqDTO.ReservationUpdateReqDTO reservationUpdateReqDTO, String sno) {
        Reservation reservation = reservationReader.readReservation(reservationId);
        if (reservationUpdateReqDTO.timeSlots() != null && !reservationUpdateReqDTO.timeSlots().isEmpty()) {
            TimeRange range = calculateTimeRange(reservationUpdateReqDTO.timeSlots());
            reservationValidator.validateTimeRange(range.start, range.end);

            // 자기 자신 제외한 중복 체크
            if (reservationRepository.existsByDateAndTimeSlotsAndIdNot(reservation.getDate(), range.start, range.end, reservation.getId())) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_TIME_CONFLICT);
            }
        }

        reservation.updateReservation(reservationUpdateReqDTO.participantList(), reservationUpdateReqDTO.purpose(), reservationUpdateReqDTO.timeSlots());
        return ReservationConverter.toReservationUpdateResDTO(reservation);
    }

    @Override
    public ReservationResDTO.ReservationDeleteResDTO deleteReservation(Long reservationId, String sno) {
        Reservation reservation = reservationReader.readReservation(reservationId);
        Member member = memberReader.readMember(sno);

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

    private TimeRange calculateTimeRange(List<LocalTime> slots) {
        LocalTime start = slots.get(0);
        LocalTime end = slots.get(slots.size() - 1).plusMinutes(30);
        return new TimeRange(start, end);
    }
}
