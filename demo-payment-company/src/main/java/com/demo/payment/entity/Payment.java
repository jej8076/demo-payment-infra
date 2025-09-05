package com.demo.payment.entity;

import com.demo.payment.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String ci;

  @Column(nullable = false)
  private String cardRefId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String sellerId;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  private LocalDateTime createdAt;

  @Builder
  public Payment(String ci, String cardRefId, BigDecimal amount, String sellerId) {
    this.ci = ci;
    this.cardRefId = cardRefId;
    this.amount = amount;
    this.sellerId = sellerId;
    this.status = PaymentStatus.REQUEST;
    this.createdAt = LocalDateTime.now();
  }

  public void changeStatus(PaymentStatus status) {
    this.status = status;
  }

}
