package com.explore.securityApp.service;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.booking.BookingInformation;
import com.explore.securityApp.dto.booking.CreateBookingRequest;
import com.explore.securityApp.dto.booking.CreateBookingResponse;
import com.explore.securityApp.dto.booking.ItineraryPrice;
import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.entity.Room;
import com.explore.securityApp.entity.User;
import com.explore.securityApp.enums.BookingStatus;
import com.explore.securityApp.enums.PriceType;
import com.explore.securityApp.exception.AlreadyExistException;
import com.explore.securityApp.exception.NotFoundException;
import com.explore.securityApp.mapper.BookingResponseMapper;
import com.explore.securityApp.repository.BookingRepository;
import com.explore.securityApp.repository.RoomRepository;
import com.explore.securityApp.repository.UserRepository;
import com.explore.securityApp.util.BookingCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingCodeGenerator bookingCodeGenerator;

    @Autowired
    private BookingResponseMapper bookingResponseMapper;

    public ApiResponse<?> createBooking(CreateBookingRequest request, String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not Found"));

        if (request.getBookingDate().isBefore(LocalDate.now())){
            throw new NotFoundException("Invalid time range");
        }

        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.APPROVED);
        statuses.add(BookingStatus.PENDING);

        List<Booking> conflictBooking = bookingRepository.findConflictBooking(
                request.getRoomId(), request.getBookingDate(), statuses , request.getEndTime(), request.getStartTime());

        if (!conflictBooking.isEmpty()){
            throw new AlreadyExistException("Room already booked in this time range");
        }

        ItineraryPrice itineraryPrice = bookingResponseMapper.constructPrice(room,request);

        Booking newBooking = new Booking();
        newBooking.setBookingDate(request.getBookingDate());
        newBooking.setStartTime(request.getStartTime());
        newBooking.setEndTime(request.getEndTime());
        newBooking.setUser(user);
        newBooking.setBasePricePerHour(itineraryPrice.getBasePricePerHour());
        newBooking.setSubTotal(itineraryPrice.getSubTotal());
        newBooking.setServiceFee(itineraryPrice.getServiceFee());
        newBooking.setTotalPrice(itineraryPrice.getTotalPrice());
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setRoom(room);

        String bookingCode = bookingCodeGenerator.generateBookingCode();
        newBooking.setBookingCode(bookingCode);

        BookingInformation response = bookingResponseMapper.constructBookingResponse(newBooking);

        bookingRepository.save(newBooking);

        return ApiResponse.success("Success booking created", response);
    }

    public ApiResponse<?> getBookingByBookingCode(String bookingCode){

        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        BookingInformation response = bookingResponseMapper.constructBookingResponse(booking);

        return ApiResponse.success("Booking found", response);

    }

}
