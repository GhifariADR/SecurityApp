package com.explore.securityApp.entity;

import com.explore.securityApp.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code")
    private String bookingCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_time" , nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time" , nullable = false)
    private LocalTime endTime;

    @Column(name = "base_price_per_hours")
    private BigDecimal basePricePerHour;

    @Column(name = "service_fee")
    private BigDecimal serviceFee;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
