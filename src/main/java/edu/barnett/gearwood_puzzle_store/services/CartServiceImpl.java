package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.CartLineDto;
import edu.barnett.gearwood_puzzle_store.dtos.CartSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.CartItem;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.exceptions.BadParameterException;
import edu.barnett.gearwood_puzzle_store.exceptions.NotFoundException;
import edu.barnett.gearwood_puzzle_store.repositories.CartItemRepository;
import edu.barnett.gearwood_puzzle_store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
public class CartServiceImpl implements CartService {

    // Requirements: 8.25% tax, $8.99 flat shipping, free at or above $75.00.
    // Adjust if requirements change
    private static final BigDecimal TAX_RATE = new BigDecimal("0.0825");
    private static final BigDecimal STANDARD_SHIPPING = new BigDecimal("8.99");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("75.00");

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           AuthService authService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.authService = authService;
    }

    @Override
    public List<CartLineDto> getCartItems() {
        return cartItemRepository.findByUser(authService.getCurrentUser())
                .stream()
                .map(CartLineDto::new)
                .toList();
    }

    /** Used to generate cart summary page */
    @Override
    public CartSummaryDto getCartSummary() {
        BigDecimal subtotal = getCartItems().stream()
                .map(CartLineDto::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                // Rounding mode not necessary for scope of product prices, but needed for scale
                .setScale(2, RoundingMode.HALF_UP);

        boolean freeShipping = subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0;
        BigDecimal shipping = freeShipping ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : STANDARD_SHIPPING;
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(shipping).add(tax);

        return new CartSummaryDto(subtotal, shipping, freeShipping, tax, total);
    }

    @Override
    @Transactional
    public void addToCart(String productCode, int quantity) {
        if (quantity <= 0) throw new BadParameterException("Quantity must be greater than zero");

        // Resolve the product entity here, in the layer that owns persistence — the controller
        // only ever passes the productCode from the form. Inactive products can't be added.
        Product product = activeProduct(productCode);

        User user = authService.getCurrentUser();
        // If this product is already in the cart, bump the existing line rather than duplicating it.
        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> new CartItem(user, product, 0));
        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void updateQuantity(String productCode, int quantity) {
        // A quantity of zero (or less) is treated as "remove from cart".
        if (quantity <= 0) {
            removeItem(productCode);
            return;
        }
        CartItem item = currentUsersLine(productCode);
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeItem(String productCode) {
        cartItemRepository.delete(currentUsersLine(productCode));
    }

    @Override
    @Transactional
    public void clearCart() {
        cartItemRepository.deleteByUser(authService.getCurrentUser());
    }

    /** Helper active product lookup for methods that prevent customers from viewing/adding inactive products */
    private Product activeProduct(String productCode) {
        return productRepository.findByProductCodeAndActiveTrue(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productCode));
    }

    /**
     * Loads the current user's cart line for the given product. Limits access to specific user so others can't view their specific combination of user and CartItem
     */
    private CartItem currentUsersLine(String productCode) {
        Product product = activeProduct(productCode);
        return cartItemRepository.findByUserAndProduct(authService.getCurrentUser(), product)
                .orElseThrow(() -> new NotFoundException("Cart item not found for product: " + productCode));
    }
}
