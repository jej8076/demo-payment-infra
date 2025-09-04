package com.demo.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardReference {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String cardRefId;

  @Column(nullable = false)
  private String encryptedCardInfo;

  private LocalDateTime createdAt;

  public CardReference(String cardRefId, String encryptedCardInfo) {
    this.cardRefId = cardRefId;
    this.encryptedCardInfo = encryptedCardInfo;
    this.createdAt = LocalDateTime.now();
  }
}
