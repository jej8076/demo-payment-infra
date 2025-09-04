package com.demo.token.service;

import com.demo.token.entity.CardReference;
import com.demo.token.entity.Token;
import com.demo.token.repository.CardReferenceRepository;
import com.demo.token.repository.TokenRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class TokenService {

  private final CardReferenceRepository cardReferenceRepository;
  private final TokenRepository tokenRepository;
  private final RestClient restClient;

  public TokenService(CardReferenceRepository cardReferenceRepository,
      TokenRepository tokenRepository, RestClient.Builder restClient) {
    this.cardReferenceRepository = cardReferenceRepository;
    this.tokenRepository = tokenRepository;
    this.restClient = restClient
        .baseUrl("http://localhost:8083")
        .build();
  }

  @Transactional
  public String createCardReference(String encryptedCardInfo) {
    String cardRefId = "card_ref_" + UUID.randomUUID();
    CardReference cardReference = new CardReference(cardRefId, encryptedCardInfo);
    cardReferenceRepository.save(cardReference);
    return cardRefId;
  }

  @Transactional
  public String generateToken(String cardRefId) {
    CardReference cardRef = cardReferenceRepository.findByCardRefId(cardRefId)
        .orElseThrow(() -> new RuntimeException("Card reference not found"));

    String tokenValue = "token_" + UUID.randomUUID().toString();
    Token token = new Token(tokenValue, cardRefId);
    tokenRepository.save(token);

    // Issuer에 토큰 승인 요청
    boolean approved = requestTokenApproval(tokenValue, cardRef.getEncryptedCardInfo());

    if (approved) {
      return tokenValue;
    } else {
      throw new RuntimeException("Token approval failed");
    }
  }

  private boolean requestTokenApproval(String tokenValue, String encryptedCardInfo) {
    try {
      String result = restClient.post()
          .uri("/issuer/approve-token")
          .body(tokenValue)
          .retrieve()
          .body(String.class);
      return "approved".equals(result);
    } catch (Exception e) {
      return true; // 데모용으로 항상 승인
    }
  }
}
