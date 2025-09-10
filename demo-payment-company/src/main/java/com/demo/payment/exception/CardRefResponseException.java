package com.demo.payment.exception;

public class CardRefResponseException extends RuntimeException {

  public CardRefResponseException() {
    super();
  }

  public CardRefResponseException(String s) {
    super(s);
  }

  public CardRefResponseException(String messageFormat, Object... args) {
    super(String.format(messageFormat, args));
  }

}
