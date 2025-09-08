package com.demo.issuer.enums;

public enum Status {
  SUCCESS, FAIL;

  public String lower() {
    return name().toLowerCase();
  }

}
