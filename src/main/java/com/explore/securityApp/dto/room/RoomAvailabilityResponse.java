package com.explore.securityApp.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityResponse {

    private LocalDate date;
    private List<RoomAvailabilityItem> items;
}
