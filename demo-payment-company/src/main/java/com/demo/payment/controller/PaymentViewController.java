package com.demo.payment.controller;

import com.demo.payment.entity.Payment;
import com.demo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentViewController {

  private final PaymentRepository paymentRepository;

  @GetMapping
  public Page<Payment> getPayments(
      @RequestParam(required = false) String ci,
      @RequestParam(required = false) String cardRefId,
      Pageable pageable) {

    if (ci != null && cardRefId != null) {
      return paymentRepository.findByCiContainingAndCardRefIdContaining(ci, cardRefId, pageable);
    } else if (ci != null) {
      return paymentRepository.findByCiContaining(ci, pageable);
    } else if (cardRefId != null) {
      return paymentRepository.findByCardRefIdContaining(cardRefId, pageable);
    }
    return paymentRepository.findAll(pageable);
  }

  @GetMapping("/{paymentId}/detail")
  public Payment getPaymentsDetail(@PathVariable(value = "paymentId") long paymentId) {
    return paymentRepository.findById(paymentId);
  }

}
