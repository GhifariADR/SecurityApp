package com.explore.securityApp.mapper;

import com.explore.securityApp.entity.Booking;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentRequestMapper {

    public Map<String, Object> constructPaymentRequest(Booking booking){
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", booking.getBookingCode());
        transactionDetails.put("gross_amount", booking.getTotalPrice());

        Map<String, Object> item = new HashMap<>();
        item.put("id", booking.getRoom().getId());
        item.put("price", booking.getBasePricePerHour());
        int hours = (int) Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
        item.put("quantity", hours);
        item.put("name", "Meeting room " + booking.getRoom().getName());

        Map<String, Object> serviceFee = new HashMap<>();
        serviceFee.put("id", "serviceFee");
        serviceFee.put("price", booking.getServiceFee());
        serviceFee.put("quantity", 1);
        serviceFee.put("name", "Service fee");

        List<Map<String, Object>> itemDetails = new ArrayList<>();
        itemDetails.add(item);
        itemDetails.add(serviceFee);


        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("transaction_details", transactionDetails);
        paymentRequest.put("item_details", itemDetails);

        return paymentRequest;
    }
}
