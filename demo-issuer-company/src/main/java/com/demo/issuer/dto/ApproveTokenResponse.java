package com.demo.issuer.dto;

import com.demo.issuer.enums.ApprovalStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApproveTokenResponse {

  private int code;
  private String message;
  private ApprovalStatus approvalStatus;

  @Builder
  public ApproveTokenResponse(int code, String message, ApprovalStatus approvalStatus) {
    this.code = code;
    this.message = message;
    this.approvalStatus = approvalStatus;
  }
}
