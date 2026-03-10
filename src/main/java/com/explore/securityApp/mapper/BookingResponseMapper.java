package com.explore.securityApp.mapper;

import com.explore.securityApp.dto.booking.BookingInformation;
import com.explore.securityApp.dto.booking.CreateBookingResponse;
import com.explore.securityApp.dto.booking.CreateBookingRoom;
import com.explore.securityApp.dto.booking.CreateBookingUser;
import com.explore.securityApp.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingResponseMapper {

    public BookingInformation constructBookingResponse(Booking booking){

        CreateBookingResponse createBookingResponse = new CreateBookingResponse();
        createBookingResponse.setBookingCode(booking.getBookingCode());
        createBookingResponse.setBookingDate(booking.getBookingDate());
        createBookingResponse.setStartTime(booking.getStartTime());
        createBookingResponse.setEndTime(booking.getEndTime());
        createBookingResponse.setStatus(booking.getStatus());

        CreateBookingUser createBookingUser = new CreateBookingUser();
        createBookingUser.setUsername(booking.getUser().getUsername());
        createBookingUser.setEmail(booking.getUser().getEmail());
        createBookingResponse.setUser(createBookingUser);

        CreateBookingRoom createBookingRoom = new CreateBookingRoom();
        createBookingRoom.setRoomName(booking.getRoom().getName());
        createBookingResponse.setRoom(createBookingRoom);

        BookingInformation response = new BookingInformation();
        response.setBookingInformation(createBookingResponse);

        return response;
    }
}
