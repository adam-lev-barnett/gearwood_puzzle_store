package edu.barnett.gearwood_puzzle_store.payment.model;

public class PaymentResult {

    private PaymentStatus status;
    private String message;
    private String transactionId;

    public PaymentResult(PaymentStatus status, String message, String transactionId) {
        this.status = status;
        this.message = message;
        this.transactionId = transactionId;
    }

    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getTransactionId() { return transactionId; }
}
