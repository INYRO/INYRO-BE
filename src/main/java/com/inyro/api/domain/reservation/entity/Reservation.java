package com.inyro.api.domain.reservation.entity;

import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.reservation.exception.ReservationErrorCode;
import com.inyro.api.domain.reservation.exception.ReservationException;
import com.inyro.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participant_list", nullable = false)
    private String participantList;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    private ReservationStatus reservationStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void validateOwner(Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_FORBIDDEN);
        }
    }

    public void updateReservation(String participantList, String purpose, List<LocalTime> timeSlots){
        if (participantList != null) this.participantList = participantList;
        if (purpose != null) this.purpose = purpose;
        if (timeSlots != null && !timeSlots.isEmpty()) {
            this.startTime = timeSlots.get(0);
            this.endTime = timeSlots.get(timeSlots.size() - 1).plusMinutes(30);
        }
    }

    public void completeReservation() {
        this.reservationStatus = ReservationStatus.COMPLETED;
    }
}
