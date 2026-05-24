package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entity.OrderData;
import edu.barnett.gearwood_puzzle_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderData, Long> {
    List<OrderData> findByUserOrderByOrderDateTimeDesc(User user);
    Optional<OrderData> findByOrderNumberAndUser(String orderNumber, User user);
    Optional<OrderData> findByOrderNumber(String orderNumber);
}
