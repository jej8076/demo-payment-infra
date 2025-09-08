package com.demo.token.exception;

public class TokenValidException extends RuntimeException {

  public TokenValidException() {
    super();
  }

  public TokenValidException(String s) {
    super(s);
  }

  public TokenValidException(String messageFormat, Object... args) {
    super(String.format(messageFormat, args));
  }

}
