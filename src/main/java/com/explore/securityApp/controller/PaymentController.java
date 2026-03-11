package com.explore.securityApp.controller;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.payment.MidtransNotification;
import com.explore.securityApp.service.PaymentService;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{bookingCode}")
    public ResponseEntity<ApiResponse<?>> payBooking(@PathVariable String bookingCode) throws MidtransError {
        return ResponseEntity.ok(paymentService.payBooking(bookingCode));
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<?>> callbackBooking(@RequestBody MidtransNotification notification) throws MidtransError {
        return ResponseEntity.ok(paymentService.callback(notification));
    }
}
