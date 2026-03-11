package com.explore.securityApp.mapper;

import com.explore.securityApp.dto.booking.*;
import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.entity.Room;
import com.explore.securityApp.enums.PriceType;
import com.explore.securityApp.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;

@Component
public class BookingResponseMapper {

    private final static Integer SERVICE_FEE_PERCENT = 5;


    public BookingInformation constructBookingResponse(Booking booking){

        CreateBookingResponse createBookingResponse = new CreateBookingResponse();
        createBookingResponse.setBookingCode(booking.getBookingCode());
        createBookingResponse.setBookingDate(booking.getBookingDate());
        createBookingResponse.setStartTime(booking.getStartTime());
        createBookingResponse.setEndTime(booking.getEndTime());
        createBookingResponse.setStatus(booking.getStatus());
        createBookingResponse.setTimeLimitPayment(booking.getTimeLimitPayment());

        CreateBookingUser createBookingUser = new CreateBookingUser();
        createBookingUser.setUsername(booking.getUser().getUsername());
        createBookingUser.setEmail(booking.getUser().getEmail());
        createBookingResponse.setUser(createBookingUser);

        CreateBookingRoom createBookingRoom = new CreateBookingRoom();
        createBookingRoom.setRoomName(booking.getRoom().getName());
        createBookingResponse.setRoom(createBookingRoom);

        ItineraryPrice price = new ItineraryPrice();
        price.setBasePricePerHour(booking.getBasePricePerHour());
        price.setSubTotal(booking.getSubTotal());
        price.setServiceFee(booking.getServiceFee());
        price.setTotalPrice(booking.getTotalPrice());
        createBookingResponse.setPrice(price);

        BookingInformation response = new BookingInformation();
        response.setBookingInformation(createBookingResponse);

        return response;
    }

    public ItineraryPrice constructPrice (Room room , CreateBookingRequest request){

        BigDecimal basePricePerHour = room.getPriceInfo().stream()
                .filter(p -> p.getPriceType()
                        .equals(isWeekend(request.getBookingDate()) ? PriceType.WEEKEND : PriceType.WEEKDAY))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Price Not Found")).getPrice();

        Long hours = Duration.between(request.getStartTime(), request.getEndTime()).toHours();

        BigDecimal subTotal = basePricePerHour.multiply(BigDecimal.valueOf(hours));

        BigDecimal serviceFee = subTotal.multiply(BigDecimal.valueOf(SERVICE_FEE_PERCENT).divide(BigDecimal.valueOf(100)));

        BigDecimal totalPrice = subTotal.add(serviceFee);

        ItineraryPrice itineraryPrice = new ItineraryPrice();
        itineraryPrice.setBasePricePerHour(basePricePerHour);
        itineraryPrice.setSubTotal(subTotal);
        itineraryPrice.setServiceFee(serviceFee);
        itineraryPrice.setTotalPrice(totalPrice);
        return itineraryPrice;
    }

    public boolean isWeekend(LocalDate date){
        DayOfWeek day = date.getDayOfWeek();

        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;

    }
}
