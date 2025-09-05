package com.demo.issuer.entity;

import com.demo.issuer.enums.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TokenApproval {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String tokenValue;

  @Enumerated(EnumType.STRING)
  private ApprovalStatus status;

  private LocalDateTime createdAt;

  @Builder
  public TokenApproval(String tokenValue, ApprovalStatus status) {
    this.tokenValue = tokenValue;
    this.status = status;
    this.createdAt = LocalDateTime.now();
  }
}
