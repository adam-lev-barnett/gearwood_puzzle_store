package com.example.puzzlestore.payment.gateway;

import com.example.puzzlestore.payment.model.PaymentRequest;
import com.example.puzzlestore.payment.model.PaymentResult;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
}
