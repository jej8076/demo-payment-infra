package com.demo.payment.dto;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class PaymentRequest {
    private String ci;
    private String cardRefId;
    private BigDecimal amount;
    private String sellerId;
}
