package com.inyro.api.domain.reservation.repository;

import com.inyro.api.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByOrderByIdDesc();

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        WHERE r.date = :date
        AND (r.startTime < :endTime AND r.endTime > :startTime)
    """)
    boolean existsByDateAndTimeSlots(@Param("date")LocalDate date, @Param("startTime") LocalTime startTime,@Param("endTime") LocalTime endTime);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        WHERE r.date = :date
        AND (r.startTime < :endTime AND r.endTime > :startTime)
        AND r.id != :reservationId
    """)
    boolean existsByDateAndTimeSlotsAndIdNot(@Param("date")LocalDate date, @Param("startTime") LocalTime startTime,@Param("endTime") LocalTime endTime, @Param("reservationId")Long reservationId);

    List<Reservation> findAllByDate(LocalDate date);

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN r.member m
        WHERE r.id = :reservationId AND m.sno = :sno
    """)
    Optional<Reservation> findByIdAndSno(@Param("reservationId")Long reservationId, @Param("sno")String sno);
}
