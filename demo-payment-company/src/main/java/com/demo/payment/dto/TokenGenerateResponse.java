package com.demo.payment.dto;

import com.demo.payment.dto.common.CommonResponse;
import com.demo.payment.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TokenGenerateResponse extends CommonResponse {

  private String token;

  public TokenGenerateResponse(int code, String message, String token, Status status) {
    super(code, message, status);
    this.token = token;
  }
}
