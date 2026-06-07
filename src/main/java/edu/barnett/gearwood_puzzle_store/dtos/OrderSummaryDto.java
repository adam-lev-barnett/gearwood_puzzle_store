package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Customer-facing view of a placed order — used for the checkout receipt, order history, and order detail pages. */
public record OrderSummaryDto(
        String orderNumber,
        LocalDateTime orderDateTime,
        OrderStatus orderStatus,
        BigDecimal totalAmount,
        String transactionId,
        ShippingInfoDto shippingInfo,
        List<OrderItemDto> items
) {
    public OrderSummaryDto(OrderData orderData) {
        this(orderData.getOrderNumber(),
                orderData.getOrderDateTime(),
                orderData.getOrderStatus(),
                orderData.getTotalAmount(),
                orderData.getTransactionID(),
                new ShippingInfoDto(orderData.getShippingInfo()),
                orderData.getOrderedItems()
                        .stream()
                        .map(OrderItemDto::new)
                        .toList());
    }

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    /** Date formatted for display, so templates don't depend on the #temporals dialect. */
    public String formattedDate() {
        return orderDateTime == null ? "" : orderDateTime.format(DISPLAY_FORMAT);
    }
}
