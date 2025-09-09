package com.demo.token.service;

import com.demo.token.dto.CardReferenceResponse;
import com.demo.token.dto.TokenGenerateResponse;
import com.demo.token.dto.TokenVerifyResponse;
import com.demo.token.dto.encrypt.HybridPayload;
import com.demo.token.encrypt.HybridDecryptor;
import com.demo.token.entity.CardReference;
import com.demo.token.entity.Token;
import com.demo.token.enums.Status;
import com.demo.token.exception.CardReferenceException;
import com.demo.token.exception.TokenValidException;
import com.demo.token.repository.CardReferenceRepository;
import com.demo.token.repository.TokenRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {

  private final CardReferenceRepository cardReferenceRepository;
  private final TokenRepository tokenRepository;
  private final HybridDecryptor hybridDecryptor;

  public TokenService(CardReferenceRepository cardReferenceRepository,
      TokenRepository tokenRepository,
      HybridDecryptor hybridDecryptor) {
    this.cardReferenceRepository = cardReferenceRepository;
    this.tokenRepository = tokenRepository;
    this.hybridDecryptor = hybridDecryptor;
  }

  @Transactional
  public TokenVerifyResponse verifyToken(String token) {
    Token tokenEntity = tokenRepository.findByTokenValue(token)
        .orElseThrow(() -> new TokenValidException("token_not_found token:%s", token));

    if (tokenEntity.isUsed()) {
      throw new TokenValidException("already_used_token token:%s", token);
    }

    // TODO expired check

    tokenEntity.changeUsed();
    tokenRepository.save(tokenEntity);

    return TokenVerifyResponse.builder()
        .code(HttpStatus.OK.value())
        .status(Status.SUCCESS)
        .message("")
        .build();
  }

  @Transactional
  public CardReferenceResponse createCardReference(HybridPayload payload) {

    String cardNumber = hybridDecryptor.decrypt(payload);

    if (!cardNumberValidator(cardNumber)) {
      throw new CardReferenceException("INVALID_CARD_NUMBER cardNumber:%s", cardNumber);
    }

    String cardRefId = "card_ref_" + UUID.randomUUID();
    CardReference cardReference = CardReference.builder()
        .cardRefId(cardRefId)
        .encryptedCardInfo(payload.getEncryptedData())
        .build();
    CardReference response = cardReferenceRepository.save(cardReference);
    return CardReferenceResponse.builder()
        .cardRefId(response.getCardRefId())
        .build();
  }

  @Transactional
  public TokenGenerateResponse generateToken(String cardRefId) {
    CardReference cardRef = cardReferenceRepository.findByCardRefId(cardRefId)
        .orElseThrow(() -> new RuntimeException("Card reference not found"));

    // TODO 책임 분리해야함
    String tokenValue = "token-" + cardRef.getCardRefId() + UUID.randomUUID();

    Token token = Token.builder()
        .cardRefId(cardRef.getCardRefId())
        .tokenValue(tokenValue)
        .build();
    tokenRepository.save(token);

    return TokenGenerateResponse.builder()
        .code(HttpStatus.OK.value())
        .message("")
        .token(tokenValue)
        .build();
  }

  private boolean cardNumberValidator(String cardNumber) {
    // TODO cardNumber에 대한 validation
    if (cardNumber.length() != 16) {
      return false;
    }

    return true;
  }
}
