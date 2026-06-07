package edu.barnett.gearwood_puzzle_store.exceptions;

public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(String message) {
        super(message);
    }
}
