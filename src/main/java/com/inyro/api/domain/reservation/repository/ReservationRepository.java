package com.inyro.api.domain.reservation.repository;

import com.inyro.api.domain.reservation.entity.Reservation;
import com.inyro.api.domain.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

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

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN r.member m
        WHERE m.sno = :sno
    """)
    Page<Reservation> findAllBySno(@Param("sno")String sno, Pageable pageable);

    @Query("""
    SELECT r
    FROM Reservation r
    WHERE r.date = :date
      AND r.reservationStatus = :status
      AND r.endTime < :time
    """)
    List<Reservation> findAllByDateAndStatusBeforeEndTime(
            @Param("date") LocalDate date,
            @Param("status") ReservationStatus status,
            @Param("time") LocalTime time
    );}
