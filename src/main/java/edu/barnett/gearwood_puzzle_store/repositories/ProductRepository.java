package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entities.Manufacturer;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    Optional<Product> findByProductCodeAndActiveTrue(String productCode);
    boolean existsByProductCode(String productCode);
    boolean existsByManufacturer(Manufacturer manufacturer);

    List<Product> findByActiveTrue();
    List<Product> findByActiveTrueAndCategory(Category category);
    List<Product> findByActiveTrueAndDifficulty(Difficulty difficulty);
    List<Product> findByActiveTrueAndPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> findByActiveTrueAndNameContainingIgnoreCase(String keyword);

    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategory(Category category);
    List<Product> findByDifficulty(Difficulty difficulty);
    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndCategory(String keyword, Category category);
    List<Product> findByNameContainingIgnoreCaseAndDifficulty(String keyword, Difficulty difficulty);
    List<Product> findByNameContainingIgnoreCaseAndPriceLessThanEqual(String keyword, BigDecimal maxPrice);
    List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String keyword, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByCategoryAndDifficulty(Category category, Difficulty difficulty);
    List<Product> findByCategoryAndPriceLessThanEqual(Category category, BigDecimal maxPrice);
    List<Product> findByCategoryAndPriceBetween(Category category, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByDifficultyAndPriceLessThanEqual(Difficulty difficulty, BigDecimal maxPrice);
    List<Product> findByDifficultyAndPriceBetween(Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndCategoryAndDifficulty(String keyword, Category category, Difficulty difficulty);
    List<Product> findByNameContainingIgnoreCaseAndCategoryAndPriceLessThanEqual(String keyword, Category category, BigDecimal maxPrice);
    List<Product> findByNameContainingIgnoreCaseAndCategoryAndPriceBetween(String keyword, Category category, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndDifficultyAndPriceLessThanEqual(String keyword, Difficulty difficulty, BigDecimal maxPrice);
    List<Product> findByNameContainingIgnoreCaseAndDifficultyAndPriceBetween(String keyword, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByCategoryAndDifficultyAndPriceLessThanEqual(Category category, Difficulty difficulty, BigDecimal maxPrice);
    List<Product> findByCategoryAndDifficultyAndPriceBetween(Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndCategoryAndDifficultyAndPriceLessThanEqual(String keyword, Category category, Difficulty difficulty, BigDecimal maxPrice);
    List<Product> findByNameContainingIgnoreCaseAndCategoryAndDifficultyAndPriceBetween(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);
}
