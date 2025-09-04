package com.demo.payment.controller;

import com.demo.payment.dto.CardRegistryRequest;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/register-card")
    public ResponseEntity<String> registerCard(@RequestBody CardRegistryRequest request) {
        String cardRefId = paymentService.registerCard(request);
        return ResponseEntity.ok(cardRefId);
    }
    
    @PostMapping("/pay")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
        String result = paymentService.processPayment(request);
        return ResponseEntity.ok(result);
    }
}
