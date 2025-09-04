package com.demo.token.controller;

import com.demo.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tsp")
@RequiredArgsConstructor
public class TokenController {
    
    private final TokenService tokenService;
    
    @PostMapping("/card-ref")
    public ResponseEntity<String> createCardReference(@RequestBody String encryptedCardInfo) {
        String cardRefId = tokenService.createCardReference(encryptedCardInfo);
        return ResponseEntity.ok(cardRefId);
    }
    
    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody String cardRefId) {
        String token = tokenService.generateToken(cardRefId);
        return ResponseEntity.ok(token);
    }
}
