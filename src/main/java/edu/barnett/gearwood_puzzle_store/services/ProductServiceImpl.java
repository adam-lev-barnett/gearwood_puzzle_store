package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.exceptions.NotFoundException;
import edu.barnett.gearwood_puzzle_store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /** Non-customers should see all products*/
    @Override
    public List<ProductSummaryDto> getAll() {
        return productRepo
                .findAll()
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    /** Primarily for customer-facing pages*/
    @Override
    public List<ProductSummaryDto> getAllActive() {
        return productRepo
                .findByActiveTrue()
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public ProductSummaryDto getByProductCode(String productCode) {
        Product product =  productRepo
                .findByProductCode(productCode)
                .orElseThrow( () -> new NotFoundException("Product not found"));
        return new ProductSummaryDto(product);
    }

    // ~~~~~~~ Search methods ~~~~~~~~~~~~~~
    @Override
    public List<ProductSummaryDto> search(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        boolean hasKeyword   = keyword != null && !keyword.isBlank();
        boolean hasCategory  = category != null;
        boolean hasDifficulty = difficulty != null;
        boolean hasMinPrice  = minPrice != null;
        boolean hasMaxPrice  = maxPrice != null;

        if (!hasKeyword && !hasCategory && !hasDifficulty && !hasMinPrice && !hasMaxPrice) return getAll();

        if (hasKeyword && hasCategory && hasDifficulty) {
            if (hasMinPrice && hasMaxPrice) return getProductsByKeywordAndCategoryAndDifficultyAndPriceRange(keyword, category, difficulty, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByKeywordAndCategoryAndDifficultyAndMaxPrice(keyword, category, difficulty, maxPrice);
            return getProductsByKeywordAndCategoryAndDifficulty(keyword, category, difficulty);
        }

        if (hasKeyword && hasCategory) {
            if (hasMinPrice && hasMaxPrice) return getProductsByKeywordAndCategoryAndPriceRange(keyword, category, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByKeywordAndCategoryAndMaxPrice(keyword, category, maxPrice);
            return getProductsByKeywordAndCategory(keyword, category);
        }

        if (hasKeyword && hasDifficulty) {
            if (hasMinPrice && hasMaxPrice) return getProductsByKeywordAndDifficultyAndPriceRange(keyword, difficulty, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByKeywordAndDifficultyAndMaxPrice(keyword, difficulty, maxPrice);
            return getProductsByKeywordAndDifficulty(keyword, difficulty);
        }

        if (hasCategory && hasDifficulty) {
            if (hasMinPrice && hasMaxPrice) return getProductsByCategoryAndDifficultyAndPriceRange(category, difficulty, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByCategoryAndDifficultyAndMaxPrice(category, difficulty, maxPrice);
            return getProductsByCategoryAndDifficulty(category, difficulty);
        }

        if (hasKeyword) {
            if (hasMinPrice && hasMaxPrice) return getProductsByKeywordAndPriceRange(keyword, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByKeywordAndMaxPrice(keyword, maxPrice);
            return getProductsByKeyword(keyword);
        }

        if (hasCategory) {
            if (hasMinPrice && hasMaxPrice) return getProductsByCategoryAndPriceRange(category, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByCategoryAndMaxPrice(category, maxPrice);
            return getProductsByCategory(category);
        }

        if (hasDifficulty) {
            if (hasMinPrice && hasMaxPrice) return getProductsByDifficultyAndPriceRange(difficulty, minPrice, maxPrice);
            if (hasMaxPrice) return getProductsByDifficultyAndMaxPrice(difficulty, maxPrice);
            return getProductsByDifficulty(difficulty);
        }
        if (hasMinPrice && hasMaxPrice) return getProductsByPriceRange(minPrice, maxPrice);

        if (hasMaxPrice) return getProductsByMaxPrice(maxPrice);
        return getAll();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeyword(String keyword) {
        return productRepo
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategory(Category category) {
        return productRepo
                .findByCategory(category)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByDifficulty(Difficulty difficulty) {
        return productRepo
                .findByDifficulty(difficulty)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByMaxPrice(BigDecimal maxPrice) {
        return productRepo
                .findByPriceLessThanEqual(maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategory(String keyword, Category category) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategory(keyword, category)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndDifficulty(String keyword, Difficulty difficulty) {
        return productRepo
                .findByNameContainingIgnoreCaseAndDifficulty(keyword, difficulty)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndMaxPrice(String keyword, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndPriceLessThanEqual(keyword, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndPriceRange(String keyword, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndPriceBetween(keyword, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategoryAndDifficulty(Category category, Difficulty difficulty) {
        return productRepo
                .findByCategoryAndDifficulty(category, difficulty)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategoryAndMaxPrice(Category category, BigDecimal maxPrice) {
        return productRepo
                .findByCategoryAndPriceLessThanEqual(category, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategoryAndPriceRange(Category category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByCategoryAndPriceBetween(category, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByDifficultyAndMaxPrice(Difficulty difficulty, BigDecimal maxPrice) {
        return productRepo
                .findByDifficultyAndPriceLessThanEqual(difficulty, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByDifficultyAndPriceRange(Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByDifficultyAndPriceBetween(difficulty, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficulty(String keyword, Category category, Difficulty difficulty) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategoryAndDifficulty(keyword, category, difficulty)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategoryAndMaxPrice(String keyword, Category category, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategoryAndPriceLessThanEqual(keyword, category, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategoryAndPriceRange(String keyword, Category category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategoryAndPriceBetween(keyword, category, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndDifficultyAndMaxPrice(String keyword, Difficulty difficulty, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndDifficultyAndPriceLessThanEqual(keyword, difficulty, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndDifficultyAndPriceRange(String keyword, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndDifficultyAndPriceBetween(keyword, difficulty, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategoryAndDifficultyAndMaxPrice(Category category, Difficulty difficulty, BigDecimal maxPrice) {
        return productRepo
                .findByCategoryAndDifficultyAndPriceLessThanEqual(category, difficulty, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByCategoryAndDifficultyAndPriceRange(Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByCategoryAndDifficultyAndPriceBetween(category, difficulty, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficultyAndMaxPrice(String keyword, Category category, Difficulty difficulty, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategoryAndDifficultyAndPriceLessThanEqual(keyword, category, difficulty, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public List<ProductSummaryDto> getProductsByKeywordAndCategoryAndDifficultyAndPriceRange(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo
                .findByNameContainingIgnoreCaseAndCategoryAndDifficultyAndPriceBetween(keyword, category, difficulty, minPrice, maxPrice)
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public void activate(String productCode) {

    }

    @Override
    public void deactivate(String productCode) {

    }

    @Override
    public void delete(String productCode) {

    }
}
