package com.demo.payment.controller;

import com.demo.payment.dto.CardRegistryRequest;
import com.demo.payment.dto.CardRegistryResponse;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/card/register")
  public ResponseEntity<CardRegistryResponse> registerCard(
      @RequestBody @Valid CardRegistryRequest request) {
    CardRegistryResponse response = paymentService.registerCard(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/pay")
  public ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
    String result = paymentService.processPayment(request);
    return ResponseEntity.ok(result);
  }
}
