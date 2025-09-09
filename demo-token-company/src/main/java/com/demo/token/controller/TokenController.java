package com.demo.token.controller;

import com.demo.token.dto.CardReferenceResponse;
import com.demo.token.dto.TokenGenerateResponse;
import com.demo.token.dto.TokenVerifyResponse;
import com.demo.token.dto.encrypt.HybridPayload;
import com.demo.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

  private final TokenService tokenService;

  @PostMapping("/card/reference")
  public ResponseEntity<CardReferenceResponse> createCardReference(
      @RequestBody HybridPayload payload) {
    CardReferenceResponse cardRefResponse = tokenService.createCardReference(payload);
    return ResponseEntity.ok(cardRefResponse);
  }

  @PostMapping("/generate")
  public ResponseEntity<TokenGenerateResponse> generateToken(@RequestBody String cardRefId) {
    TokenGenerateResponse tokenGenerateResponse = tokenService.generateToken(cardRefId);
    return ResponseEntity.ok(tokenGenerateResponse);
  }

  @PostMapping("/verify")
  public ResponseEntity<TokenVerifyResponse> verifyToken(@RequestBody String token) {
    TokenVerifyResponse verifyToken = tokenService.verifyToken(token);
    return ResponseEntity.ok(verifyToken);
  }

}
