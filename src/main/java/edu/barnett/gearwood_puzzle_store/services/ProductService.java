package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllActive();
    List<Product> getAll();
    Optional<Product> getByProductCode(String productCode);
    List<Product> search(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);
    Product save(Product product);
    void activate(String productCode);
    void deactivate(String productCode);
    void delete(String productCode);
}
