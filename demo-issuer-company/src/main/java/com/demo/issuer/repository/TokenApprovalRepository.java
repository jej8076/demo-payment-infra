package com.demo.issuer.repository;

import com.demo.issuer.entity.TokenApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenApprovalRepository extends JpaRepository<TokenApproval, Long> {
}
