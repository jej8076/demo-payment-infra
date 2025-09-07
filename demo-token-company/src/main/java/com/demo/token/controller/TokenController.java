package com.demo.token.controller;

import com.demo.token.dto.CardReferenceResponse;
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
  public ResponseEntity<String> generateToken(@RequestBody String cardRefId) {
    String token = tokenService.generateToken(cardRefId);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/verify")
  public ResponseEntity<String> verifyToken(@RequestBody String token) {
    String message = tokenService.verifyToken(token);
    return ResponseEntity.ok(message);
  }

}
