package com.demo.token.dto;

import com.demo.token.dto.common.CommonResponse;
import com.demo.token.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TokenVerifyResponse extends CommonResponse {

  private Status status;

  public TokenVerifyResponse(int code, String message, Status status) {
    super(code, message);
    this.status = status;
  }
}
