package edu.barnett.gearwood_puzzle_store.dtos;

import java.math.BigDecimal;

/** Order-summary totals for the cart sidebar. Money math lives in CartService, not here. */
public record CartSummaryDto(
        BigDecimal subtotal,
        BigDecimal shipping,
        boolean freeShipping,
        BigDecimal tax,
        BigDecimal total) {
}
