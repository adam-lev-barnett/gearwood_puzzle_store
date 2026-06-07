package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.OrderItem;

import java.math.BigDecimal;

public record OrderItemDto(String productCode,
                           String imgSrc,
                           String productName,
                           BigDecimal unitPrice,
                           Integer quantity,
                           BigDecimal lineTotal) {
    public OrderItemDto(OrderItem orderItem){
        // The product may have been deleted since the order; the line keeps its own name/price snapshot.
        this(orderItem.getProduct() != null ? orderItem.getProduct().getProductCode() : null,
                orderItem.getProduct() != null ? orderItem.getProduct().getPrimaryImage() : null,
                orderItem.getProductName(),
                orderItem.getUnitPriceUponPurchase(),
                orderItem.getQuantity(),
                orderItem.getLineTotal());
    }
}
