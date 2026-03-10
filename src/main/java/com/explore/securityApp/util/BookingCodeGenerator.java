package com.explore.securityApp.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BookingCodeGenerator {

    private static final String CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateBookingCode(){
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6 ; i++){
            int index = RANDOM.nextInt(CHARACTER.length());
            code.append(CHARACTER.charAt(index));
        }

        return code.toString();
    }
}
