#!/bin/bash

echo "Starting all services..."

# Payment Company (Port 8081)
cd demo-payment-company
./gradlew bootRun &
PAYMENT_PID=$!

# Token Company (Port 8082)
cd ../demo-token-company
./gradlew bootRun &
TOKEN_PID=$!

# Issuer Company (Port 8083)
cd ../demo-issuer-company
./gradlew bootRun &
ISSUER_PID=$!

echo "All services started!"
echo "Payment Company: http://localhost:8081"
echo "Token Company: http://localhost:8082"
echo "Issuer Company: http://localhost:8083"

echo "Press Ctrl+C to stop all services"

# Wait for interrupt
trap "kill $PAYMENT_PID $TOKEN_PID $ISSUER_PID; exit" INT
wait
