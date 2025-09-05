package com.demo.token.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardReferenceResponse {
  private String cardRefId;

  @Builder
  public CardReferenceResponse(String cardRefId) {
    this.cardRefId = cardRefId;
  }
}
