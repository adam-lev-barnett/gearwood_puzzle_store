package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderSummaryDto(
        String orderNumber,
        UserDto user,
        OrderStatus status,
        BigDecimal totalAmount,
        String transactionId,
        ShippingInfoDto shippingInfo,
        List<OrderItemDto> orderItems
) {
    public OrderSummaryDto(OrderData orderData) {
        this(orderData.getOrderNumber(),
                new UserDto(orderData.getUser()),
                orderData.getOrderStatus(),
                orderData.getTotalAmount(),
                orderData.getTransactionID(),
                new ShippingInfoDto(orderData.getShippingInfo()),
                orderData.getOrderedItems()
                        .stream()
                        .map(OrderItemDto::new)
                        .toList()
                        );
    }
}
