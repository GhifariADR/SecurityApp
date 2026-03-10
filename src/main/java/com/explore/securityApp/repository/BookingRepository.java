package com.explore.securityApp.repository;

import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.enums.BookingStatus;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndBookingDateAndStatusIn(
            Long roomId,
            LocalDate date,
            List<BookingStatus> statuses
    );

    @Query("select b from Booking b " + "where b.room.id = :roomId "
            + "and b.bookingDate = :date "+ "and b.status IN :statuses " + "AND (b.startTime < :endTime AND b.endTime > :startTime)")
    List<Booking> findConflictBooking(
        Long roomId,
        LocalDate date,
        List<BookingStatus> statuses,
        LocalTime endTime,
        LocalTime startTime
    );

    Optional<Booking> findByBookingCode(String bookingCode);
}
