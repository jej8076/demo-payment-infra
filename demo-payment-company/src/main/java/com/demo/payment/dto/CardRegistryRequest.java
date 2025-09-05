package com.demo.payment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRegistryRequest {
    private String ci;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
