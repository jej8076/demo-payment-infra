package com.demo.issuer.service;

import com.demo.issuer.entity.TokenApproval;
import com.demo.issuer.repository.TokenApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalService {
    
    private final TokenApprovalRepository tokenApprovalRepository;
    
    @Transactional
    public String approveToken(String tokenValue) {
        // 실제로는 카드 정보 검증 로직 구현
        TokenApproval.ApprovalStatus status = validateToken(tokenValue) ? 
                TokenApproval.ApprovalStatus.APPROVED : TokenApproval.ApprovalStatus.REJECTED;
        
        TokenApproval approval = new TokenApproval(tokenValue, status);
        tokenApprovalRepository.save(approval);
        
        return status == TokenApproval.ApprovalStatus.APPROVED ? "approved" : "rejected";
    }
    
    @Transactional
    public String verifyToken(String tokenValue) {
        // 토큰 검증 로직
        return "verified";
    }
    
    private boolean validateToken(String tokenValue) {
        // 데모용으로 항상 승인
        return true;
    }
}
