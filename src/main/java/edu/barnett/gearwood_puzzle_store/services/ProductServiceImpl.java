package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.ProductAdminDto;
import edu.barnett.gearwood_puzzle_store.dtos.ProductCreateRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.Manufacturer;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.exceptions.AlreadyExistsException;
import edu.barnett.gearwood_puzzle_store.exceptions.NotFoundException;
import edu.barnett.gearwood_puzzle_store.repositories.CartItemRepository;
import edu.barnett.gearwood_puzzle_store.repositories.ManufacturerRepository;
import edu.barnett.gearwood_puzzle_store.repositories.OrderItemRepository;
import edu.barnett.gearwood_puzzle_store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CartItemRepository cartItemRepo;
    private final OrderItemRepository orderItemRepo;
    private final ManufacturerRepository manufacturerRepo;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo, CartItemRepository cartItemRepo,
                              OrderItemRepository orderItemRepo, ManufacturerRepository manufacturerRepo) {
        this.productRepo = productRepo;
        this.cartItemRepo = cartItemRepo;
        this.orderItemRepo = orderItemRepo;
        this.manufacturerRepo = manufacturerRepo;
    }

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
    public List<ProductSummaryDto> getFeaturedProducts() {
        return productRepo
                .findByActiveTrueAndFeaturedTrue()
                .stream()
                .map(ProductSummaryDto::new)
                .toList();
    }

    @Override
    public ProductSummaryDto getByProductCode(String productCode) {
        return new ProductSummaryDto(findProductOrThrow(productCode));
    }

    @Override
    public ProductAdminDto getByProductCodeAsAdmin(String productCode) {
        return new ProductAdminDto(findProductOrThrow(productCode));
    }

    private Product findProductOrThrow(String productCode) {
        return productRepo
                .findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    // ~~~~~~~ Search methods ~~~~~~~~~~~~~~
    /** Goes through entire set of filter options and fields and determines which searches and filters are needed based on filled and empty fields in the form*/
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

    @Transactional
    @Override
    public void updateProduct(ProductCreateRequestDto productCreateRequest) {
        Product product = productRepo.findByProductCode(productCreateRequest.productCode())
                .orElseThrow( () -> new NotFoundException("Product cannot be updated because product doesn't exist"));
        setProductFields(productCreateRequest, product);
    }

    @Transactional
    @Override
    public void setProductFields(ProductCreateRequestDto productCreateRequest, Product product) {
        product.setProductCode(productCreateRequest.productCode());
        product.setName(productCreateRequest.name());
        Manufacturer manufacturer = manufacturerRepo.findByName(productCreateRequest.manufacturer())
                .orElseThrow(() -> new NotFoundException("Manufacturer not found: " + productCreateRequest.manufacturer()));
        product.setManufacturer(manufacturer);
        product.setNumberOfPieces(productCreateRequest.numberOfPieces());
        product.setDifficulty(productCreateRequest.difficulty());
        product.setCategory(productCreateRequest.category());
        product.setPrice(productCreateRequest.price());
        product.setShortDescription(productCreateRequest.shortDescription());
        product.setLongDescription(productCreateRequest.longDescription());
        product.setAcquiredDate(productCreateRequest.acquiredDate());
        // active, featured, and primaryImgSource are deliberately left untouched here so an edit
        // can't wipe them — they're managed elsewhere (activate/deactivate, curation, seed data).
        productRepo.save(product);
    }

    @Transactional
    @Override
    public ProductAdminDto createProduct(ProductCreateRequestDto productCreateRequest) {
        if (productRepo.existsByProductCode(productCreateRequest.productCode())) throw new AlreadyExistsException("Product already exists");
        Product product = new Product();
        // New products start visible and un-featured; the create form doesn't manage these fields.
        product.setActive(true);
        product.setFeatured(false);
        // Creating new manufacturers wasn't a requirement for this project, so to keep things simple
        // the admin picks from existing manufacturers via a dropdown; setProductFields matches that
        // chosen name to a Manufacturer entity rather than letting the admin create one here.
        setProductFields(productCreateRequest, product);
        return new ProductAdminDto(product);
    }

    @Transactional
    @Override
    public void activate(String productCode) {
        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow( () -> new NotFoundException("Product does not exist"));
        product.setActive(true);
        productRepo.save(product);
    }

    @Transactional
    @Override
    public void deactivate(String productCode) {
        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow( () -> new NotFoundException("Product does not exist"));
        product.setActive(false);
        productRepo.save(product);
    }

    @Transactional
    @Override
    public boolean delete(String productCode) {
        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow( () -> new NotFoundException("Product does not exist"));
        // Note: we deactivate (no exception) so the change actually commits — throwing here
        // would roll back the very deactivation we just made within this @Transactional method.
        if (cartItemRepo.existsByProduct(product) || orderItemRepo.existsByProduct(product)) {
            product.setActive(false);
            productRepo.save(product);
            return false;
        }
        productRepo.delete(product);
        return true;
    }
}
