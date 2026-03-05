package com.explore.securityApp.service;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.auth.RegisterRequest;
import com.explore.securityApp.dto.room.*;
import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.entity.Room;
import com.explore.securityApp.enums.BookingStatus;
import com.explore.securityApp.enums.RoomStatus;
import com.explore.securityApp.exception.AlreadyExistException;
import com.explore.securityApp.repository.BookingRepository;
import com.explore.securityApp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public ApiResponse<?> register(RoomRegisterRequest request){

        if (roomRepository.findByName(request.getName()).isPresent()){
            throw new AlreadyExistException("Room's name is already exist");
        }

        Room room =new Room();
        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setStatus(RoomStatus.UNAVAILABLE);

        roomRepository.save(room);

        return ApiResponse.success("Room successfully created", null);

    }

    public ApiResponse<?> getAvailability(RoomAvailabilityRequest request){

        List<Room> rooms = roomRepository.findAllByStatus(RoomStatus.AVAILABLE);
        List<RoomAvailabilityItem> result = new ArrayList<>();

        List<BookingStatus> statuses = new ArrayList<>();
        statuses.add(BookingStatus.APPROVED);
        statuses.add(BookingStatus.PENDING);

        for(Room room : rooms){
            List<Booking> bookings = bookingRepository.findByRoomIdAndBookingDateAndStatusIn(
                    room.getId(),
                    request.getDate(),
                    statuses
            );

            List<LocalTime> availableSlot = generateSlots();
            List<LocalTime> bookedSlot = new ArrayList<>();

            for (Booking booking : bookings){
                LocalTime start = booking.getStartTime();
                while (start.isBefore(booking.getEndTime())){
                    bookedSlot.add(start);
                    start = start.plusHours(1);
                }
            }

            List<RoomAvailableSlots> availableSlots = availableSlot.stream()
                    .map(slot -> new RoomAvailableSlots(
                            slot.toString(),
                            !bookedSlot.contains(slot)
                    ))
                    .collect(Collectors.toList());

            result.add(
                    new RoomAvailabilityItem(
                            room.getId(),
                            room.getName(),
                            availableSlots
                    )
            );

        }

        RoomAvailabilityResponse responses = new RoomAvailabilityResponse();
        responses.setDate(request.getDate());
        responses.setRoomAvailability(result);

        return ApiResponse.success("Success", responses);

    }

    public List<LocalTime> generateSlots(){
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(17,0);

        while (start.isBefore(end)){
            slots.add(start);
            start = start.plusHours(1);
        }

        return slots;

    }

}
