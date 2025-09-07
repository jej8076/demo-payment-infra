package com.demo.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRegistryRequest {

  @NotBlank(message = "ci is required")
  private String ci;
  @NotBlank(message = "cardNumber is required")
  private String cardNumber;
  @NotBlank(message = "expiryDate is required")
  private String expiryDate;
  @NotBlank(message = "cvv is required")
  private String cvv;
}
