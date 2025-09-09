package com.demo.payment.dto.common;

import com.demo.payment.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CommonResponse {

  private int code;
  private String message;
  private Status status;

  public CommonResponse(int code, String message, Status status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
