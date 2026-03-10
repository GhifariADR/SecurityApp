package com.explore.securityApp.dto.booking;

import com.explore.securityApp.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponse {

    private String bookingCode;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private ItineraryPrice price;
    private CreateBookingUser user;
    private CreateBookingRoom room;
}
