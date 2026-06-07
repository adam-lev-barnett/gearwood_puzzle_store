package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.PendingAddToCart;
import edu.barnett.gearwood_puzzle_store.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Separates logic from the ProductController to decouple the two; both handle different logic
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

    /** Generates cart page */
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cart", cartService.getCartSummary());
        return "cart";
    }

    /** permitAll() overrides the class-level isAuthenticated() so anonymous shoppers can reach this method, which will then prompt them to log in */
    @PostMapping("/add")
    @PreAuthorize("permitAll()")
    public String addToCart(@RequestParam String productCode,
                            @RequestParam(defaultValue = "1") int quantity,
                            @AuthenticationPrincipal UserDetails user,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        // from the session right after a successful login.
        // Anonymous shopper in not stored as UserDetails, so it resolves to null.
        if (user == null) {
            // Session-save the intended item in a transitive cart, and send them to log in;
            session.setAttribute("pendingCartAdd", new PendingAddToCart(productCode, quantity));
            redirectAttributes.addFlashAttribute("infoMessage", "Please log in to add items to your cart.");
            return "redirect:/login";
        }
        try {
            cartService.addToCart(productCode, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Item added to cart.");
        } catch (Exception ex) {
            // Send them back to the product's detail page with the reason (e.g. the
            // product is inactive). The detail route is /products/{code} — "productDetails"
            // is the view name, not a URL.
            redirectAttributes.addFlashAttribute("errorMessage", "Could not add item: " + ex.getMessage());
            return "redirect:/products/" + productCode;
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
