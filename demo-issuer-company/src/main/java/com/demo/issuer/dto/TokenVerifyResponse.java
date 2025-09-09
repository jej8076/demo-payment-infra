package com.demo.issuer.dto;

import com.demo.issuer.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenVerifyResponse {

  private int code;
  private String message;
  private Status status;

  @Builder
  public TokenVerifyResponse(int code, String message, Status status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
