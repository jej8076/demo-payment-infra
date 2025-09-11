package com.demo.payment.service;

import com.demo.payment.dto.ApproveTokenResponse;
import com.demo.payment.dto.CardReferenceResponse;
import com.demo.payment.dto.CardRegistryRequest;
import com.demo.payment.dto.CardRegistryResponse;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.dto.TokenGenerateRequest;
import com.demo.payment.dto.TokenGenerateResponse;
import com.demo.payment.dto.encrypt.HybridPayload;
import com.demo.payment.encrypt.HybridEncryptor;
import com.demo.payment.entity.Payment;
import com.demo.payment.enums.ApprovalStatus;
import com.demo.payment.enums.PaymentStatus;
import com.demo.payment.enums.Status;
import com.demo.payment.exception.CardRefResponseException;
import com.demo.payment.exception.IssuerResponseException;
import com.demo.payment.exception.TokenResponseException;
import com.demo.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final PaymentSaverService paymentSaverService;
  private final HybridEncryptor hybridEncryptor;
  private final RestClient tokenRestClient;
  private final RestClient issuerRestClient;
  private final String TOKEN_CARD_REF = "/token/card/reference";
  private final String TOKEN_GENERATE = "/token/generate";
  private final String ISSUER_APPROVE_TOKEN = "/issuer/approve/token";

  public PaymentService(PaymentRepository paymentRepository,
      PaymentSaverService paymentSaverService, HybridEncryptor hybridEncryptor,
      @Qualifier("tokenRestClient") RestClient tokenClient,
      @Qualifier("issuerRestClient") RestClient issuerClient) {
    this.paymentRepository = paymentRepository;
    this.paymentSaverService = paymentSaverService;
    this.hybridEncryptor = hybridEncryptor;
    this.tokenRestClient = tokenClient;
    this.issuerRestClient = issuerClient;
  }

  /**
   * <pre>
   * 조건 : 카드정보는 페이 결제사에서 볼 수 없으며 암호화하여 토큰관리사에게 전달한다
   * 구현 :
   * 1. 암호화는 RSA(서명 역할)와 AES128(암호화 역할)를 조합하여 사용하도록 한다
   * 2. 페이결제사에서는 IV,KEY 값을 갖고 있지 않고 즉시 생성하여 전달하기 때문에 보관 시 유출될 가능성을 없앴다
   * 3. 페이결제사에서는 RSA의 공개키만 가지고 있기 때문에 복호화가 불가능한 상태
   * 4. 토큰관리사에서 RSA 비밀키를 가지고 있기 때문에 복호화하여 카드정보 확인 가능
   * 5. 페이결제사에서 IV,KEY값도 토큰관리사에게 함께 전달하기 때문에 복호화하여 카드정보 확인 가능
   * </pre>
   *
   * @param request
   * @return
   */
  @Transactional
  public CardRegistryResponse registerCard(CardRegistryRequest request) {

    ajustCardInfo(request);

    // TODO 카드번호에 대해 Luhn 알고리즘을 이용하여 validation

    // ${ci}_${cardNumber} 형태
    String cardInfo = concatCardInfo(request);

    // 카드 번호 암호화
    HybridPayload encryptedCardNumber = hybridEncryptor.encrypt(cardInfo);

    // 토큰 관리사에 요청
    CardReferenceResponse cardRefResponse = requestCardRefId(encryptedCardNumber);

    return CardRegistryResponse.builder()
        .cardRefId(cardRefResponse.getCardRefId())
        .build();
  }

  @Transactional
  public String processPayment(PaymentRequest request) {
    Payment payment = Payment.builder()
        .ci(request.getCi())
        .cardRefId(request.getCardRefId())
        .amount(request.getAmount())
        .sellerId(request.getSellerId())
        .build();

    paymentSaverService.saveByNewTransaction(payment);

    TokenGenerateRequest generateRequest = TokenGenerateRequest.builder()
        .ci(request.getCi())
        .cardRefId(request.getCardRefId())
        .build();

    // 토큰 요청
    // TODO 실패 시 실패 이유를 응답받도록 해야함
    TokenGenerateResponse tokenGenerateResponse = requestToken(generateRequest);

    ApproveTokenResponse approval = requestApproval(tokenGenerateResponse.getToken());

    if (ApprovalStatus.REJECTED.equals(approval.getApprovalStatus())) {
      payment.changeStatus(PaymentStatus.REJECTED);
      paymentRepository.save(payment);
      return Status.FAIL.lower();
    }

    payment.changeStatus(PaymentStatus.APPROVED);

    // 명시적으로 영속성 컨텍스트에 입력함 (dirty checking은 누군가에게 오해받을 수 있기 때문)
    paymentRepository.save(payment);

    return Status.SUCCESS.lower();
  }

  private String concatCardInfo(CardRegistryRequest request) {
    return request.getCi() + "_" + request.getCardNumber();
  }

  private CardReferenceResponse requestCardRefId(HybridPayload payload) {
    Optional<CardReferenceResponse> restResponse = tokenRestClient.post()
        .uri(TOKEN_CARD_REF)
        .body(payload)
        .exchange((request, response) ->
            Optional.ofNullable(response.bodyTo(CardReferenceResponse.class))
        );

    if (restResponse.isEmpty()) {
      throw new TokenResponseException("토큰 서비스의 잘못된 응답 -> %s", TOKEN_CARD_REF);
    }

    CardReferenceResponse cardRefResponse = restResponse.get();
    if (cardRefResponse.getCode() != HttpStatus.OK.value()) {
      throw new CardRefResponseException("CardRefId 발급 실패 message:%s",
          cardRefResponse.getMessage());
    }

    return restResponse.get();
  }

  private TokenGenerateResponse requestToken(TokenGenerateRequest generateRequest) {

    Optional<TokenGenerateResponse> restResponse = tokenRestClient.post()
        .uri(TOKEN_GENERATE)
        .body(generateRequest)
        .exchange((request, response) ->
            Optional.ofNullable(response.bodyTo(TokenGenerateResponse.class))
        );

    if (restResponse.isEmpty()) {
      throw new TokenResponseException("BAD_RESPONSE_TOKEN -> %s", TOKEN_GENERATE);
    }

    if (restResponse.get().getCode() != 200) {
      throw new TokenResponseException("INVALID_RESPONSE_TOKEN message:%s",
          restResponse.get().getMessage());
    }

    return restResponse.get();
  }

  private ApproveTokenResponse requestApproval(String cardRefId) {
    Optional<ApproveTokenResponse> restResponse = issuerRestClient.post()
        .uri(ISSUER_APPROVE_TOKEN)
        .body(cardRefId)
        .exchange((request, response) ->
            Optional.ofNullable(response.bodyTo(ApproveTokenResponse.class))
        );

    if (restResponse.isEmpty()) {
      throw new IssuerResponseException("BAD_RESPONSE_ISSUER -> %s", ISSUER_APPROVE_TOKEN);
    }

    ApproveTokenResponse approveResponse = restResponse.get();

    if (ApprovalStatus.REJECTED.equals(approveResponse.getApprovalStatus())) {
      throw new IssuerResponseException("INVALID_RESPONSE_ISSUER message:%s",
          approveResponse.getMessage());
    }

    return approveResponse;
  }

  private void ajustCardInfo(CardRegistryRequest request) {
    String cardNumber = request.getCardNumber();
    cardNumber = cardNumber.replace("-", "");
    request.changeCardNumber(cardNumber);
  }

}
