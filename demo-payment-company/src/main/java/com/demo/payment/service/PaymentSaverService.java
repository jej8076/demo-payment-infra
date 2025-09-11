package com.demo.payment.service;

import com.demo.payment.entity.Payment;
import com.demo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentSaverService {

  private final PaymentRepository paymentRepository;

  // 독립적인 트랜잭션 실행
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveByNewTransaction(Payment payment) {
    paymentRepository.save(payment);
  }
}