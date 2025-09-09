package com.demo.payment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenGenerateRequest {

  private String ci;
  private String cardRefId;

  @Builder
  public TokenGenerateRequest(String ci, String cardRefId) {
    this.ci = ci;
    this.cardRefId = cardRefId;
  }
}
