package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.entities.User;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(User user);
    void addToCart(User user, Product product, int quantity);
    void updateQuantity(User user, Long cartItemId, int quantity);
    void removeItem(User user, Long cartItemId);
    void clearCart(User user);
}
