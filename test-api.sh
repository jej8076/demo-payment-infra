#!/bin/bash

echo "Testing Payment API..."

# 1. Register Card
echo "1. Registering card..."
CARD_REF_ID=$(curl -s -X POST http://localhost:8081/payment/register-card \
  -H "Content-Type: application/json" \
  -d '{
    "ci": "user123",
    "cardNumber": "1234567890123456",
    "expiryDate": "12/25",
    "cvv": "123"
  }')

echo "Card Ref ID: $CARD_REF_ID"

# 2. Process Payment
echo "2. Processing payment..."
PAYMENT_RESULT=$(curl -s -X POST http://localhost:8081/payment/pay \
  -H "Content-Type: application/json" \
  -d "{
    \"ci\": \"user123\",
    \"cardRefId\": \"$CARD_REF_ID\",
    \"amount\": 10000,
    \"merchantId\": \"merchant001\"
  }")

echo "Payment Result: $PAYMENT_RESULT"
