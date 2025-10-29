package com.inyro.api.domain.reservation.repository;

import com.inyro.api.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        WHERE r.date = :date
        AND (r.startTime < :endTime AND r.endTime > :startTime)
    """)
    boolean existsByDateAndTimeSlots(@Param("date")LocalDate date, @Param("startTime") LocalTime startDate,@Param("endTime") LocalTime endDate);

    List<Reservation> findAllByDate(LocalDate date);
}
