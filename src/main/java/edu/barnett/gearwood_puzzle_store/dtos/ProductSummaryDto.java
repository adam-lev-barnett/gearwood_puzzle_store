package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;

public record ProductSummaryDto(
                                String productCode,
                                String name,
                                String manufacturer,
                                Category category,
                                Difficulty difficulty,
                                BigDecimal price,
                                String shortDescription,
                                String imgSrc,
                                boolean isActive) {

    public ProductSummaryDto(Product product) {
        this(
                product.getProductCode(),
                product.getName(),
                product.getManufacturer().getName(),
                product.getCategory(),
                product.getDifficulty(),
                product.getPrice(),
                product.getShortDescription(),
                product.getPrimaryImage(),
                product.isActive()
                );
    }
}

