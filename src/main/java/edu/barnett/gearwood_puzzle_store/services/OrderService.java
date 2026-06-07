package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.OrderSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.User;

import java.util.List;

public interface OrderService {
    /** All of a user's orders, most recent first. */
    List<OrderSummaryDto> getOrdersByUser(User user);

    /** A single order belonging to the given user. Scoped by user so one customer can't view another's order. */
    OrderSummaryDto getOrderByOrderNumber(String orderNumber, User user);
}
