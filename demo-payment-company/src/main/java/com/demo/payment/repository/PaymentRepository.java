package com.demo.payment.repository;

import com.demo.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Page<Payment> findByCiContaining(String ci, Pageable pageable);

  Page<Payment> findByCardRefIdContaining(String cardRefId, Pageable pageable);

  Page<Payment> findByCiContainingAndCardRefIdContaining(String ci, String cardRefId,
      Pageable pageable);

  Payment findById(long paymentId);
}
