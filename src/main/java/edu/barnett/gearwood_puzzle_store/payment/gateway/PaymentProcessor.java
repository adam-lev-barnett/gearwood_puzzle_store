package edu.barnett.gearwood_puzzle_store.payment.gateway;

import edu.barnett.gearwood_puzzle_store.payment.model.PaymentRequest;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentResult;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
}
