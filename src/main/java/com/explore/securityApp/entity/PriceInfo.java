package com.explore.securityApp.entity;

import com.explore.securityApp.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "price_info")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private BigDecimal price;

    @Column(name = "price_type")
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
}
