package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.OrderSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentRequest;

public interface CheckoutService {
    OrderSummaryDto processCheckout(User user, ShippingInfo shippingInfo, PaymentRequest paymentRequest);

    String generateOrderNumber();
}
