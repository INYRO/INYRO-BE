package com.inyro.api.domain.reservation.scheduler;

import com.inyro.api.domain.reservation.service.command.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationCommandService reservationCommandService;

    @Scheduled(cron = "0 0/30 * * * *")
    public void reservationScheduler() {try {
        log.info("[ReservationScheduler] 만료 예약 처리 시작");
        reservationCommandService.updateExpiredReservations();
        log.info("[ReservationScheduler] 만료 예약 처리 완료");
    } catch (Exception e) {
        log.error("[ReservationScheduler] 만료 예약 처리 중 오류 발생", e);
    }
    }
}
