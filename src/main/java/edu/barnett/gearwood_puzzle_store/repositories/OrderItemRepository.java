package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entities.OrderItem;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProduct(Product product);
}
