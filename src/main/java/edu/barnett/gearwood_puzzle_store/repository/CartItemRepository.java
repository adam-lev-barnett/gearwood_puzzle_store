package edu.barnett.gearwood_puzzle_store.repository;

import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entity.Product;
import edu.barnett.gearwood_puzzle_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    boolean existsByProduct(Product product);
    void deleteByUser(User user);
}
