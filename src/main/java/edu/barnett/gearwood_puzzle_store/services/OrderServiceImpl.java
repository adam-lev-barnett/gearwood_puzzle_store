package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.OrderSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.OrderData;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.exceptions.NotFoundException;
import edu.barnett.gearwood_puzzle_store.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // @Transactional keeps the Hibernate session open while OrderSummaryDto is built — its
    // constructor reads the lazy orderedItems collection, and open-in-view is disabled.
    @Override
    @Transactional
    public List<OrderSummaryDto> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateTimeDesc(user)
                .stream()
                .map(OrderSummaryDto::new)
                .toList();
    }

    @Override
    @Transactional
    public OrderSummaryDto getOrderByOrderNumber(String orderNumber, User user) {
        OrderData order = orderRepository.findByOrderNumberAndUser(orderNumber, user)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderNumber));
        return new OrderSummaryDto(order);
    }
}
