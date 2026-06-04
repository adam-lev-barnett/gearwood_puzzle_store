package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.CartLineDto;
import edu.barnett.gearwood_puzzle_store.dtos.CartSummaryDto;

import java.util.List;

/** All operations act on the currently authenticated user's cart, resolved internally via AuthService. */
public interface CartService {
    List<CartLineDto> getCartItems();
    CartSummaryDto getCartSummary();
    void addToCart(String productCode, int quantity);
    void updateQuantity(String productCode, int quantity);
    void removeItem(String productCode);
    void clearCart();
}
