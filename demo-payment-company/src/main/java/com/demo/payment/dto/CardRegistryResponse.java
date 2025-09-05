package com.demo.payment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRegistryResponse {
  private String cardRefId;

  @Builder
  public CardRegistryResponse(String cardRefId) {
    this.cardRefId = cardRefId;
  }
}
