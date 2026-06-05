package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.OrderItem;

import java.math.BigDecimal;

public record OrderItemDto(String productCode,
                           String productName,
                           BigDecimal priceUponPurchase,
                           Integer quantity,
                           BigDecimal lineTotal) {
    public OrderItemDto(OrderItem orderItem){
        this(orderItem.getProduct().getProductCode(),
                orderItem.getProductName(),
                orderItem.getUnitPriceUponPurchase(),
                orderItem.getQuantity(),
                orderItem.getLineTotal());
    }
}
