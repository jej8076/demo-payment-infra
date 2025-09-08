package com.demo.token.enums;

public enum Status {
  SUCCESS, FAIL;

  public String lower() {
    return name().toLowerCase();
  }

}
