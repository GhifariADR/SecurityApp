package com.explore.securityApp.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MidtransNotification {

    private String status_code;
    private String order_id;
    private String transaction_status;
    private String payment_type;
    private String fraud_status;
    private String gross_amount;
}
