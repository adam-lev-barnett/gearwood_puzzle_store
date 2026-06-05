package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.PendingCartAdd;
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

    // permitAll() overrides the class-level isAuthenticated() so anonymous shoppers
    // can reach this method — we handle their auth state ourselves below.
    @PostMapping("/add")
    @PreAuthorize("permitAll()")
    public String addToCart(@RequestParam String productCode,
                            @RequestParam(defaultValue = "1") int quantity,
                            @AuthenticationPrincipal UserDetails user,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        // Anonymous shopper: the principal isn't a UserDetails, so it resolves to null.
        // Stash the intended item and send them to log in; HomeController replays it
        // from the session right after a successful login.
        if (user == null) {
            session.setAttribute("pendingCartAdd", new PendingCartAdd(productCode, quantity));
            redirectAttributes.addFlashAttribute("infoMessage", "Please log in to add items to your cart.");
            return "redirect:/login";
        }
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
