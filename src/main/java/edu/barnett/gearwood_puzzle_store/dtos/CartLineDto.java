package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;

/** One line in the cart: a product snapshot plus the quantity and computed line total. */
public record CartLineDto(
        String productCode,
        String name,
        String imgSrc,
        Category category,
        Difficulty difficulty,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal) {

    public CartLineDto(CartItem item) {
        this(
                item.getProduct().getProductCode(),
                item.getProduct().getName(),
                item.getProduct().getPrimaryImage(),
                item.getProduct().getCategory(),
                item.getProduct().getDifficulty(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
