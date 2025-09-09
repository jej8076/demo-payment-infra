package com.demo.payment.enums;

public enum Status {
  SUCCESS, FAIL;

  public String lower() {
    return name().toLowerCase();
  }

}
