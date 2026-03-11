package com.explore.securityApp.service;

import com.explore.securityApp.config.MidtransWebClientConfiguration;
import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.payment.*;
import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.enums.BookingStatus;
import com.explore.securityApp.exception.AlreadyExistException;
import com.explore.securityApp.exception.BadRequestException;
import com.explore.securityApp.exception.NotFoundException;
import com.explore.securityApp.mapper.PaymentRequestMapper;
import com.explore.securityApp.repository.BookingRepository;
import com.midtrans.Config;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    @Autowired
    private MidtransWebClientConfiguration midtransWebClientConfiguration;

    private final WebClient webClient;
    @Value("${midtrans.server.key}")
    private String SERVER_KEY;

    @Value("${midtrans.client.key}")
    private String CLIENT_KEY;

    @Value("${midtrans.is-production}")
    private boolean IS_PRODUCTION;

    public PaymentService(WebClient webClient) {
        this.webClient = webClient;
    }


    @Transactional
    public ApiResponse<?> payBooking(String bookingCode) throws MidtransError {

        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new NotFoundException("Booking not Found"));

        if (booking.getStatus().equals(BookingStatus.REJECTED)){
            throw new AlreadyExistException("Booking is already rejected");
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED)){
            throw new AlreadyExistException("Booking is already approved");
        }

        Map<String, Object> paymentRequest = paymentRequestMapper.constructPaymentRequest(booking);

        Config config = new Config(SERVER_KEY,CLIENT_KEY,IS_PRODUCTION);

        String url = SnapApi.createTransactionRedirectUrl(paymentRequest, config);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setRedirect_url(url);

        return ApiResponse.success("Success", paymentResponse);

    }

    @Async
    @Transactional
    public ApiResponse<?> callback(MidtransNotification midtransNotification) throws MidtransError{
        Booking booking = bookingRepository.findByBookingCode(midtransNotification.getOrder_id())
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        String status = midtransNotification.getTransaction_status();

        if ("settlement".equals(status) || "capture".equals(status)) {

            booking.setStatus(BookingStatus.APPROVED);
            bookingRepository.save(booking);
        }

        return ApiResponse.success("Booking Processed", null);

    }

    public ApiResponse<?> checkPaymentStatus(String bookingCode) throws MidtransError{

        PaymentStatusResponse paymentStatusResponse =
                midtransWebClientConfiguration.midtransClient()
                        .get()
                        .uri(bookingCode + "/status")
                        .retrieve()
                        .bodyToMono(PaymentStatusResponse.class)
                        .block();

        return ApiResponse.success("Booking Processed", paymentStatusResponse);

    }
}
