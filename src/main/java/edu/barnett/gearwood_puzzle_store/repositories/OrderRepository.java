package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderData, Long> {
    List<OrderData> findByUserOrderByOrderDateTimeDesc(User user);
    Optional<OrderData> findByOrderNumberAndUser(String orderNumber, User user);
}
