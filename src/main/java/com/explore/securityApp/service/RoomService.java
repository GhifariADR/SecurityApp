package com.explore.securityApp.service;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.auth.RegisterRequest;
import com.explore.securityApp.dto.room.RoomRegisterRequest;
import com.explore.securityApp.entity.Room;
import com.explore.securityApp.enums.RoomStatus;
import com.explore.securityApp.exception.AlreadyExistException;
import com.explore.securityApp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

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
}
