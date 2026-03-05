package com.explore.securityApp.repository;

import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndBookingDateAndStatus(
            Long roomId,
            LocalDate date,
            BookingStatus status
    );
}
