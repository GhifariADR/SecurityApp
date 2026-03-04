package com.explore.securityApp.controller;

import com.explore.securityApp.dto.ApiResponse;
import com.explore.securityApp.dto.room.RoomRegisterRequest;
import com.explore.securityApp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> roomRegister(@Valid @RequestBody RoomRegisterRequest request){
        return ResponseEntity.ok().body(roomService.register(request));
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }
}
