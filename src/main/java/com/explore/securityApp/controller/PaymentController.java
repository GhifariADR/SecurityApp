package com.explore.securityApp.controller;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.service.PaymentService;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{bookingCode}")
    public ResponseEntity<ApiResponse<?>> payBooking(@PathVariable String bookingCode) throws MidtransError {
        return ResponseEntity.ok(paymentService.payBooking(bookingCode));
    }
}
