package com.explore.securityApp.controller;

import com.explore.securityApp.dto.auth.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tes")
public class TesController {

    @PostMapping
    public ResponseEntity<?> tes (){
        return ResponseEntity.ok(new LoginResponse("tes","tes"));
    }
}
