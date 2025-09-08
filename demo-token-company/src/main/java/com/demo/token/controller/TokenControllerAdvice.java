package com.demo.token.controller;

import static com.demo.token.utils.IpUtils.getClientIp;

import com.demo.token.dto.common.CommonResponse;
import com.demo.token.exception.TokenValidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.demo.token")
@RequiredArgsConstructor
@Slf4j
public class TokenControllerAdvice {

  @ExceptionHandler(TokenValidException.class)
  public ResponseEntity TokenValidException(HttpServletRequest request, TokenValidException ex) {
    log.error("[TOKEN-ERROR] ip={}, message={}", getClientIp(request), ex.getMessage());
    String message = String.format("토큰 유효성 검사에 실패하였습니다.[%s]", ex.getMessage());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(CommonResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(message)
            .build()
        );
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleValidationExceptions(HttpServletRequest request,
      MethodArgumentNotValidException ex) {
    log.error("[TOKEN-ERROR] ip={}, message={}", getClientIp(request), ex.getMessage());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(CommonResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(createErrorDesc(ex.getBindingResult()))
            .build()
        );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity unHandleExceptions(HttpServletRequest request, Exception ex) {
    log.error("[Unhandled Exception] ip={}, message={}", getClientIp(request), ex.getMessage(),
        ex);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("잘못된 요청입니다.");
  }

  private String createErrorDesc(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .toList()
        .toString();
  }
}
