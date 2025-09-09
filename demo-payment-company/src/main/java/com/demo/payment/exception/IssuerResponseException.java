package com.demo.payment.exception;

public class IssuerResponseException extends RuntimeException {

  public IssuerResponseException() {
    super();
  }

  public IssuerResponseException(String s) {
    super(s);
  }

  public IssuerResponseException(String messageFormat, Object... args) {
    super(String.format(messageFormat, args));
  }

}
