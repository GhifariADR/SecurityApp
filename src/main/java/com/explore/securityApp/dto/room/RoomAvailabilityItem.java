package com.explore.securityApp.dto.room;

import com.explore.securityApp.entity.PriceInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityItem {
    private Long roomId;
    private String roomName;
    private BigDecimal priceInfo;
    private List<RoomAvailableSlots> availableSlots;
}
