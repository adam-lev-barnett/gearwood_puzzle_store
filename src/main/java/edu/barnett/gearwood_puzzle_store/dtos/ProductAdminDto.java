package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Product;

import java.time.LocalDate;

/**
 * Admin-facing product view. Wraps the customer-safe {@link ProductSummaryDto}
 * and adds fields that must never be exposed to regular customers (e.g. acquiredDate).
 * Only build/pass this DTO from admin-only controller paths.
 */
public record ProductAdminDto(ProductSummaryDto summary,
                              LocalDate acquiredDate) {

    public ProductAdminDto(Product product) {
        this(new ProductSummaryDto(product), product.getAcquiredDate());
    }
}
