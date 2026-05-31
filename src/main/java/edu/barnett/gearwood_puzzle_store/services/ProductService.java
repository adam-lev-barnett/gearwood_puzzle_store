package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.ProductAdminDto;
import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductSummaryDto> getAllActive();
    List<ProductSummaryDto> getAll();
    ProductSummaryDto getByProductCode(String productCode);

    /** Admin-only fetch: includes admin fields (e.g. acquiredDate) not exposed to customers. */
    ProductAdminDto getByProductCodeAsAdmin(String productCode);

    /* Dispatcher — null fields are ignored, falls back to getAll() if everything is null */
    List<ProductSummaryDto> search(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    /* Named filter methods */
    List<ProductSummaryDto> getProductsByKeyword(String keyword);
    List<ProductSummaryDto> getProductsByCategory(Category category);
    List<ProductSummaryDto> getProductsByDifficulty(Difficulty difficulty);
    List<ProductSummaryDto> getProductsByMaxPrice(BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByKeywordAndCategory(String keyword, Category category);
    List<ProductSummaryDto> getProductsByKeywordAndDifficulty(String keyword, Difficulty difficulty);
    List<ProductSummaryDto> getProductsByKeywordAndMaxPrice(String keyword, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByKeywordAndPriceRange(String keyword, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByCategoryAndDifficulty(Category category, Difficulty difficulty);
    List<ProductSummaryDto> getProductsByCategoryAndMaxPrice(Category category, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByCategoryAndPriceRange(Category category, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByDifficultyAndMaxPrice(Difficulty difficulty, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByDifficultyAndPriceRange(Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficulty(String keyword, Category category, Difficulty difficulty);
    List<ProductSummaryDto> getProductsByKeywordAndCategoryAndMaxPrice(String keyword, Category category, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByKeywordAndCategoryAndPriceRange(String keyword, Category category, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByKeywordAndDifficultyAndMaxPrice(String keyword, Difficulty difficulty, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByKeywordAndDifficultyAndPriceRange(String keyword, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByCategoryAndDifficultyAndMaxPrice(Category category, Difficulty difficulty, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByCategoryAndDifficultyAndPriceRange(Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficultyAndMaxPrice(String keyword, Category category, Difficulty difficulty, BigDecimal maxPrice);
    List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficultyAndPriceRange(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice);


    Product save(Product product);
    void activate(String productCode);
    void deactivate(String productCode);
    void delete(String productCode);
}
