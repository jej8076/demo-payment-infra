package com.demo.payment.dto;

import com.demo.payment.dto.common.CommonResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CardReferenceResponse extends CommonResponse {

  private String cardRefId;

  public CardReferenceResponse(String cardRefId) {
    this.cardRefId = cardRefId;
  }
}
