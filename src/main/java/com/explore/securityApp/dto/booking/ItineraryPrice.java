package com.explore.securityApp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryPrice {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal basePricePerHour;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal serviceFee;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal subTotal;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal totalPrice;

}
