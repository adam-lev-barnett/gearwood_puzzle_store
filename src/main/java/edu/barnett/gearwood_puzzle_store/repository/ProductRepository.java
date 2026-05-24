package edu.barnett.gearwood_puzzle_store.repository;

import edu.barnett.gearwood_puzzle_store.entity.Manufacturer;
import edu.barnett.gearwood_puzzle_store.entity.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
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
}
