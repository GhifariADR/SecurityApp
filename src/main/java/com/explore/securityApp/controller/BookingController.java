package com.explore.securityApp.controller;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.booking.CreateBookingRequest;
import com.explore.securityApp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/createBooking")
    public ResponseEntity<ApiResponse<?>> createBooking
            (@Valid @RequestBody CreateBookingRequest request, Authentication authentication){
        return ResponseEntity.ok(bookingService.createBooking(request, authentication.getName()));
    }
}
