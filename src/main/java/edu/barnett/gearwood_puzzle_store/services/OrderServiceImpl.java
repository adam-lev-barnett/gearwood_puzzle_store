package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
    @Override
    public OrderData createOrder(User user, List<CartItem> cartItems, ShippingInfo shippingInfo, PaymentResult paymentResult) {
        return null;
    }

    @Override
    public List<OrderData> getOrdersByUser(User user) {
        return List.of();
    }

    @Override
    public Optional<OrderData> getOrderByOrderNumber(String orderNumber, User user) {
        return Optional.empty();
    }
}
