package com.explore.securityApp.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailableSlots {

    private String time;
    private boolean isAvailable;

}
