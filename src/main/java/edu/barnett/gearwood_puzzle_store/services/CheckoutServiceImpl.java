package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.CartSummaryDto;
import edu.barnett.gearwood_puzzle_store.dtos.OrderSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.*;
import edu.barnett.gearwood_puzzle_store.enums.OrderStatus;
import edu.barnett.gearwood_puzzle_store.exceptions.BadParameterException;
import edu.barnett.gearwood_puzzle_store.exceptions.PaymentDeclinedException;
import edu.barnett.gearwood_puzzle_store.payment.gateway.DummyPaymentProcessor;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentRequest;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentResult;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentStatus;
import edu.barnett.gearwood_puzzle_store.repositories.CartItemRepository;
import edu.barnett.gearwood_puzzle_store.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final DummyPaymentProcessor paymentProcessor;

    public CheckoutServiceImpl(CartService cartService,
                               CartItemRepository cartItemRepository,
                               OrderRepository orderRepository,
                               DummyPaymentProcessor paymentProcessor) {
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    @Transactional
    public OrderSummaryDto processCheckout(User user, ShippingInfo shippingInfo, PaymentRequest paymentRequest) {
        List<CartItem> cart = cartItemRepository.findByUser(user);
        if (cart.isEmpty()) throw new BadParameterException("Cannot check out an empty cart");

        // Totals are read from the cart's own pricing so the tax/shipping rules stay in one place.
        // The DTO is consumed locally here and mapped onto the order entity — it isn't passed on.
        CartSummaryDto totals = cartService.getCartSummary();

        // The checkout form doesn't submit an amount; charge the server-computed total
        // (never trust a client-supplied amount). The processor validates amount > 0.
        paymentRequest.setAmount(totals.total());

        PaymentResult paymentResult = paymentProcessor.processPayment(paymentRequest);
        if (paymentResult.getStatus() == PaymentStatus.DECLINED)
            throw new PaymentDeclinedException("The payment was declined. Please try again");

        OrderData order = new OrderData(
                generateOrderNumber(),
                user,
                LocalDateTime.now(),
                OrderStatus.PAID,
                totals.subtotal(),
                totals.tax(),
                totals.shipping(),
                totals.total(),
                paymentResult.getTransactionId(),
                shippingInfo);

        // Snapshot each cart line into an immutable order line (name + price at purchase time).
        for (CartItem item : cart) {
            Product product = item.getProduct();
            order.addOrderItem(new OrderItem(
                    order, product, product.getName(), product.getPrice(), item.getQuantity()));
        }

        OrderData saved = orderRepository.save(order); // cascade persists the order items
        cartService.clearCart();                       // empty the cart now that it's an order

        return new OrderSummaryDto(saved);
    }

    /** Application-defined order number (ORD-XXXXXXXX) that never exposes the DB id. */
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
