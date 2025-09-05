package com.demo.issuer.service;

import com.demo.issuer.entity.TokenApproval;
import com.demo.issuer.enums.ApprovalStatus;
import com.demo.issuer.repository.TokenApprovalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class ApprovalService {

  private final TokenApprovalRepository tokenApprovalRepository;
  private final RestClient restClient;

  public ApprovalService(TokenApprovalRepository tokenApprovalRepository,
      RestClient.Builder restClient) {
    this.tokenApprovalRepository = tokenApprovalRepository;
    this.restClient = restClient
        .baseUrl("http://localhost:8001")
        .build();
  }

  /**
   * 결제 과정에서 카드정보를 사용하면 안됨
   *
   * @param tokenValue
   * @return
   */
  @Transactional
  public String approveToken(String tokenValue) {

    ApprovalStatus status = "success".equals(validateToken(tokenValue)) ?
        ApprovalStatus.APPROVED : ApprovalStatus.REJECTED;

    TokenApproval approval = TokenApproval.builder()
        .tokenValue(tokenValue)
        .status(status)
        .build();
    tokenApprovalRepository.save(approval);

    return status == ApprovalStatus.APPROVED ? "approved" : "rejected";
  }

  private String validateToken(String tokenValue) {
    return restClient.post()
        .uri("/token/verify")
        .body(tokenValue)
        .retrieve()
        .body(String.class);
  }
}
