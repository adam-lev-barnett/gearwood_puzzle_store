package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> getAllActive() {
        return List.of();
    }

    @Override
    public List<Product> getAll() {
        return List.of();
    }

    @Override
    public Optional<Product> getByProductCode(String productCode) {
        return Optional.empty();
    }

    @Override
    public List<Product> search(String keyword, Category category, Difficulty difficulty, BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
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
