package com.demo.token.exception;

public class CardReferenceException extends RuntimeException {

  public CardReferenceException() {
    super();
  }

  public CardReferenceException(String s) {
    super(s);
  }

  public CardReferenceException(String messageFormat, Object... args) {
    super(String.format(messageFormat, args));
  }

}