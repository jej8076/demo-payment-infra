package com.demo.payment.exception;

public class TokenResponseException extends RuntimeException {

  public TokenResponseException() {
    super();
  }

  public TokenResponseException(String s) {
    super(s);
  }

  public TokenResponseException(String messageFormat, Object... args) {
    super(String.format(messageFormat, args));
  }

}
