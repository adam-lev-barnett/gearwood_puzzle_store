package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.utils.ImageMap;

import java.math.BigDecimal;
import java.util.List;

public record ProductSummaryDto(
        String productCode,
        String name,
        String manufacturer,
        Category category,
        Difficulty difficulty,
        BigDecimal price,
        String shortDescription,
        String longDescription,
        Integer numberOfPieces,
        String imgSrc,
        boolean isActive,
        List<String> images) {

    public ProductSummaryDto(Product product) {
        this(
                product.getProductCode(),
                product.getName(),
                product.getManufacturer().getName(),
                product.getCategory(),
                product.getDifficulty(),
                product.getPrice(),
                product.getShortDescription(),
                product.getLongDescription(),
                product.getNumberOfPieces(),
                product.getPrimaryImage(),
                product.isActive(),
                ImageMap.getProductImages().getOrDefault(product.getProductCode(), List.of()));
    }
}

