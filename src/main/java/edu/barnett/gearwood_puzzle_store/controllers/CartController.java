package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Owns the /cart URL space. The "Add to Cart" buttons on the catalog and product detail pages
 * post here rather than to ProductController — the cart is the resource being mutated, and this
 * keeps ProductController read-only and product-focused.
 */
@Controller
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cart", cartService.getCartSummary());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String productCode,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(productCode, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Item added to cart.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not add item: " + ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam String productCode,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(productCode, quantity);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not update item: " + ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam String productCode,
                             RedirectAttributes redirectAttributes) {
        try {
            cartService.removeItem(productCode);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not remove item: " + ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
