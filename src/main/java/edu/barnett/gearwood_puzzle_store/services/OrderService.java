package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentResult;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderData createOrder(User user, List<CartItem> cartItems, ShippingInfo shippingInfo, PaymentResult paymentResult);
    List<OrderData> getOrdersByUser(User user);
    Optional<OrderData> getOrderByOrderNumber(String orderNumber, User user);
}
