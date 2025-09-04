package com.demo.payment.service;

import com.demo.payment.dto.CardRegistryRequest;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.entity.Payment;
import com.demo.payment.enums.PaymentStatus;
import com.demo.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final RestClient restClient;

  public PaymentService(PaymentRepository paymentRepository, RestClient.Builder restClient) {
    this.paymentRepository = paymentRepository;
    this.restClient = restClient
        .baseUrl("http://localhost:8082")
        .build();
  }

  @Transactional
  public String registerCard(CardRegistryRequest request) {
    // TSP에 암호화된 카드 정보 전달하여 Card_Ref_ID 요청
    String encryptedCardInfo = encryptCardInfo(request);
    return requestCardRefId(encryptedCardInfo);
  }

  @Transactional
  public String processPayment(PaymentRequest request) {
    Payment payment = Payment.builder()
        .ci(request.getCi())
        .cardRefId(request.getCardRefId())
        .amount(request.getAmount())
        .merchantId(request.getMerchantId())
        .build();

    paymentRepository.save(payment);

    // 토큰 요청
    String token = requestToken(request.getCardRefId());

    if (token != null) {
      payment.changeStatus(PaymentStatus.APPROVED);
      return "Payment approved with token: " + token;
    } else {
      payment.changeStatus(PaymentStatus.REJECTED);
      return "Payment rejected";
    }
  }

  private String encryptCardInfo(CardRegistryRequest request) {
    // TODO 암호화하여 응답하도록 수정 필요
    return "encrypted_" + request.getCardNumber();
  }

  private String requestCardRefId(String encryptedCardInfo) {
    try {
      return restClient.post()
          .uri("/tsp/card-ref")
          .body(encryptedCardInfo)
          .retrieve()
          .body(String.class);
    } catch (Exception e) {
      return "card_ref_" + System.currentTimeMillis();
    }
  }

  private String requestToken(String cardRefId) {
    try {
      return restClient.post()
          .uri("/tsp/token")
          .body(cardRefId)
          .retrieve()
          .body(String.class);
    } catch (Exception e) {
      return "token_" + System.currentTimeMillis();
    }
  }
}
