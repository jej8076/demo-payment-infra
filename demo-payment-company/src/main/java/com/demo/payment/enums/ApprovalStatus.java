package com.demo.payment.enums;

public enum ApprovalStatus {
  APPROVED, REJECTED;

  public String lower() {
    return name().toLowerCase();
  }
}
