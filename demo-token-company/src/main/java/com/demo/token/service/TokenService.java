package com.demo.token.service;

import com.demo.token.dto.CardReferenceResponse;
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
        .baseUrl("http://localhost:8002")
        .build();
  }

  @Transactional
  public CardReferenceResponse createCardReference(String encryptedCardInfo) {

    String cardRefId = "card_ref_" + UUID.randomUUID();

    CardReference cardReference = CardReference.builder()
        .cardRefId(cardRefId)
        .encryptedCardInfo(encryptedCardInfo)
        .build();
    CardReference response = cardReferenceRepository.save(cardReference);
    return CardReferenceResponse.builder()
        .cardRefId(response.getCardRefId())
        .build();
  }

  @Transactional
  public String generateToken(String cardRefId) {
    CardReference cardRef = cardReferenceRepository.findByCardRefId(cardRefId)
        .orElseThrow(() -> new RuntimeException("Card reference not found"));

    // TODO 책임 분리해야함
    String tokenValue = "token-" + cardRef + UUID.randomUUID();

    Token token = Token.builder()
        .cardRefId(cardRef.getCardRefId())
        .tokenValue(tokenValue)
        .build();
    tokenRepository.save(token);

    return tokenValue;
  }

  @Transactional
  public String verifyToken(String token) {
    Token tokenEntity = tokenRepository.findByTokenValue(token)
        .orElseThrow(() -> new RuntimeException("Token not found"));

    if (tokenEntity.isUsed()) {
      throw new RuntimeException("already used token ->" + token);
    }

    tokenEntity.changeUsed();
    tokenRepository.save(tokenEntity);
    return "success";
  }
}
