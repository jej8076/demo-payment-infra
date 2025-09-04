package com.demo.payment.dto;

import lombok.Getter;

@Getter
public class CardRegistryRequest {
    private String ci;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
