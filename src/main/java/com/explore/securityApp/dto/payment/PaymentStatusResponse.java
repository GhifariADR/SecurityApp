package com.explore.securityApp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponse {

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("gross_amount")
    private String grossAmount;

    private String currency;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("signature_key")
    private String signatureKey;

    @JsonProperty("transaction_status")
    private String transactionStatus;

    @JsonProperty("fraud_status")
    private String fraudStatus;

    @JsonProperty("status_message")
    private String statusMessage;

    @JsonProperty("merchant_id")
    private String merchantId;

    @JsonProperty("transaction_type")
    private String transactionType;

    private String issuer;
    private String acquirer;

    @JsonProperty("transaction_time")
    private String transactionTime;

    @JsonProperty("settlement_time")
    private String settlementTime;

    @JsonProperty("expiry_time")
    private String expiryTime;

}
