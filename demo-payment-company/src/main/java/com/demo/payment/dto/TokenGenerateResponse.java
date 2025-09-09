package com.demo.payment.dto;

import com.demo.payment.dto.common.CommonResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TokenGenerateResponse extends CommonResponse {

  private String token;

  public TokenGenerateResponse(int code, String message, String token) {
    super(code, message);
    this.token = token;
  }
}
