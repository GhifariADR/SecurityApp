package com.explore.securityApp.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull
    private Long roomId;

    @NotNull
    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @NotNull
    @Column(name = "start_time")
    private LocalTime startTime;

    @NotNull
    @Column(name = "start_time")
    private LocalTime endTime;

}
