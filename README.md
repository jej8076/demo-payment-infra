### spring docs(payment) : http://localhost:8000/swagger-ui/index.html#/

### spring docs(token) : http://localhost:8001/swagger-ui/index.html#/

### spring docs(issuer) : http://localhost:8002/swagger-ui/index.html#/

### payment list page : http://localhost:3000

## 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant 사용자
    participant TR as 페이 결제사
    participant TSP as 토큰 관리사
    participant Issuer as 결제 승인사

% 카드 생성
사용자 ->> TR: /payment/card/register(카드생성)
TR ->> TR: RSA+AES128 카드정보 암호화(공개키사용)
TR ->> TSP: /token/card/reference(cardRefId요청)
TSP ->> TSP: RSA+AES128 카드정보 복호화(비밀키사용)
TSP ->> TSP: cardRefId 생성
TSP ->> TR: cardRefId응답
TR ->> 사용자: cardRefId 전달

% 결제
사용자 ->> TR: /payment/pay(결제요청)
TR ->> TSP: /token/generate(토큰생성요청)
TSP ->> TR: token 응답
TR ->> Issuer: /issuer/approve/token(결제승인요청)
Issuer ->> TSP: /token/verify(토큰유효성검사요청)
TSP ->> Issuer: 토큰 유효성 검사 응답
Issuer ->> TR: 결제 승인 응답
TR ->> 사용자: 결제 완료 응답 
```

## ERD

```mermaid
classDiagram
    class PAYMENT {
        ID BIGINT
        AMOUNT(NUMERIC)
        CARD_REF_ID(VARCHAR)
        CI(VARCHAR)
        CREATED_AT(TIMESTAMP)
        SELLER_ID(VARCHAR)
        STATUS(VARCHAR)
    }

    class CARD_REFERENCE {
        ID BIGINT
        CARD_REF_ID(VARCHAR)[unique]
        CREATED_AT(TIMESTAMP)
        ENCRYPTED_CARD_INFO(VARCHAR)[unique]
    }

    class TOKEN {
        ID BIGINT
        CARD_REF_ID(VARCHAR)
        CREATED_AT(TIMESTAMP)
        EXPIRES_AT(TIMESTAMP)
        TOKEN_VALUE(VARCHAR)[unique]
        USED(BOOLEAN)
    }

    class TOKEN_APPROVAL {
        ID BIGINT
        CREATED_AT(TIMESTAMP)
        STATUS(VARCHAR)
        TOKEN_VALUE(VARCHAR)
    }
```
