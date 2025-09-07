package com.demo.payment.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRequest {

  private String ci;
  private String cardRefId;
  private BigDecimal amount;
  private String sellerId;
}
