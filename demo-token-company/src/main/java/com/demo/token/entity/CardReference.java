package com.demo.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardReference {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String ci;

  @Column(unique = true, nullable = false)
  private String cardRefId;

  @Column(nullable = false)
  private String encryptedCardInfo;

  private LocalDateTime createdAt;

  @Builder
  public CardReference(Long id, String ci, String cardRefId, String encryptedCardInfo,
      LocalDateTime createdAt) {
    this.id = id;
    this.ci = ci;
    this.cardRefId = cardRefId;
    this.encryptedCardInfo = encryptedCardInfo;
    this.createdAt = LocalDateTime.now();
  }
}
