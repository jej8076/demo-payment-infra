package com.demo.token.dto.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse {

  private int code;
  private String message;

  @Builder
  public CommonResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
