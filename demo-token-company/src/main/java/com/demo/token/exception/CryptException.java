package com.demo.token.exception;

public class CryptException extends RuntimeException {

  public CryptException() {
    super();
  }

  public CryptException(String s) {
    super(s);
  }

  public CryptException(String var1, Throwable var2) {
    super(var1, var2);
  }
}