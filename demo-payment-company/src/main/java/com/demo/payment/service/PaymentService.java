package com.demo.payment.service;

import com.demo.payment.dto.CardReferenceResponse;
import com.demo.payment.dto.CardRegistryRequest;
import com.demo.payment.dto.CardRegistryResponse;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.entity.Payment;
import com.demo.payment.enums.PaymentStatus;
import com.demo.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final RestClient tokenRestClient;
  private final RestClient issuerRestClient;

  public PaymentService(PaymentRepository paymentRepository,
      @Qualifier("tokenRestClient") RestClient tokenClient,
      @Qualifier("issuerRestClient") RestClient issuerClient) {
    this.paymentRepository = paymentRepository;
    this.tokenRestClient = tokenClient;
    this.issuerRestClient = issuerClient;
  }

  /**
   * <pre>
   * 조건 : 카드정보는 페이 결제사에서 볼 수 없으며 암호화하여 토큰관리사에게 전달한다
   * 페이결제사(현재 모듈)에서 카드정보를 볼 수 없도록 하려면 페이 결제사에 카드 정보 데이터가 전달되기 전
   * 암호화 모듈이 있어야 할 것으로 생각된다
   * </pre>
   *
   * @param request
   * @return
   */
  @Transactional
  public CardRegistryResponse registerCard(CardRegistryRequest request) {
    // TODO 카드번호에 대해 Luhn 알고리즘을 이용하여 validation, 만약 암호화되어 전달받는다면 validation 하지 못하고 토큰 관리사로 bypass 한다

    // 카드 번호 암호화
    String encryptedCardInfo = encryptCardInfo(request);

    // 토큰 관리사에 요청
    CardReferenceResponse cardRefResponse = requestCardRefId(encryptedCardInfo);

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

    paymentRepository.save(payment);

    // 토큰 요청
    // TODO 실패 시 실패 이유를 응답받도록 해야함
    String token = requestToken(request.getCardRefId());

    if (token == null) {
      payment.changeStatus(PaymentStatus.REJECTED);
      paymentRepository.save(payment);
      return "fail";
    }

    String approvalState = requestApproval(token);

    if ("fail".equals(approvalState)) {
      payment.changeStatus(PaymentStatus.REJECTED);
      paymentRepository.save(payment);
      return approvalState;
    }

    payment.changeStatus(PaymentStatus.APPROVED);

    // 명시적으로 영속성 컨텍스트에 입력함 (dirty checking은 누군가에게 오해받을 수 있기 때문)
    paymentRepository.save(payment);

    return "";
  }

  private String encryptCardInfo(CardRegistryRequest request) {
    // TODO 암호화하여 응답하도록 수정 필요
    return "encrypted_" + request.getCardNumber();
  }

  private CardReferenceResponse requestCardRefId(String encryptedCardInfo) {
    try {
      return tokenRestClient.post()
          .uri("/token/card/reference")
          .body(encryptedCardInfo)
          .retrieve()
          .body(CardReferenceResponse.class);

    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  private String requestToken(String cardRefId) {
    try {
      return tokenRestClient.post()
          .uri("/token/generate")
          .body(cardRefId)
          .retrieve()
          .body(String.class);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  private String requestApproval(String cardRefId) {
    try {
      return issuerRestClient.post()
          .uri("/issuer/approve/token")
          .body(cardRefId)
          .retrieve()
          .body(String.class);
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

}
