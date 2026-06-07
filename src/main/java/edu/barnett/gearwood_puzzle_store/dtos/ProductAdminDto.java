package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Product;

import java.time.LocalDate;

/**
 * Admin-facing product view. Wraps the customer-safe {@link ProductSummaryDto}
 * and adds fields that shouldn't be shown regular customers (e.g. acquiredDate,
 * featured). Only build/pass this DTO from admin-specific endpoints.
 */
public record ProductAdminDto(ProductSummaryDto summary,
                              LocalDate acquiredDate,
                              boolean featured) {

    public ProductAdminDto(Product product) {
        this(new ProductSummaryDto(product), product.getAcquiredDate(), product.isFeatured());
    }
}
