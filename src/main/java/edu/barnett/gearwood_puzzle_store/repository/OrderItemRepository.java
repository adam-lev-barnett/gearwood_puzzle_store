package edu.barnett.gearwood_puzzle_store.repository;

import edu.barnett.gearwood_puzzle_store.entity.OrderItem;
import edu.barnett.gearwood_puzzle_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProduct(Product product);
}
