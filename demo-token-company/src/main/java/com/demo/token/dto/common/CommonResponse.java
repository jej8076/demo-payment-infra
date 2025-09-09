package com.demo.token.dto.common;

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

  public CommonResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
