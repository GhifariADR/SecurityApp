package com.explore.securityApp.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityItem {
    private Long roomId;
    private String roomName;
    private List<RoomAvailableSlots> availableSlots;
}
