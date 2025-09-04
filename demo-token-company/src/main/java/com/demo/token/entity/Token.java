package com.demo.token.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String tokenValue;
    
    @Column(nullable = false)
    private String cardRefId;
    
    @Column(nullable = false)
    private boolean used;
    
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    
    public Token(String tokenValue, String cardRefId) {
        this.tokenValue = tokenValue;
        this.cardRefId = cardRefId;
        this.used = false;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(10); // 10분 유효
    }
    
    public void markAsUsed() {
        this.used = true;
    }
}
