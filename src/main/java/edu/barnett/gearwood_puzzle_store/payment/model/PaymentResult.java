package edu.barnett.gearwood_puzzle_store.payment.model;

public class PaymentResult {

    private final PaymentStatus status;
    private final String message;
    private final String transactionId;

    public PaymentResult(PaymentStatus status, String message, String transactionId) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
    }

    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getTransactionId() { return transactionId; }
}
