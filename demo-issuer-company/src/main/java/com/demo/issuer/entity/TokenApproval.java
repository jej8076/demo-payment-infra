package com.demo.issuer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    
    public TokenApproval(String tokenValue, ApprovalStatus status) {
        this.tokenValue = tokenValue;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
    
    public enum ApprovalStatus {
        APPROVED, REJECTED
    }
}
