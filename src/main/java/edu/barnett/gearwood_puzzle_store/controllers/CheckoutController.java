package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.CartLineDto;
import edu.barnett.gearwood_puzzle_store.dtos.OrderSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;
import edu.barnett.gearwood_puzzle_store.exceptions.PaymentDeclinedException;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentRequest;
import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.CartService;
import edu.barnett.gearwood_puzzle_store.services.CheckoutService;
import edu.barnett.gearwood_puzzle_store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/checkout")
@PreAuthorize("isAuthenticated()")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartService cartService;
    private final OrderService orderService;
    private final AuthService authService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, CartService cartService,
                             OrderService orderService, AuthService authService) {
        this.checkoutService = checkoutService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.authService = authService;
    }

    /** Renders the checkout page */
    @GetMapping
    public String showCheckout(Model model) {
        List<CartLineDto> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) return "redirect:/cart"; // nothing to check out
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cart", cartService.getCartSummary());
        return "checkout";
    }

    @PostMapping
    public String checkout(@ModelAttribute ShippingInfo shippingInfo, @ModelAttribute PaymentRequest paymentRequest,
                           RedirectAttributes redirectAttributes) {
        OrderSummaryDto order;
        try {
            order = checkoutService.processCheckout(authService.getCurrentUser(), shippingInfo, paymentRequest);
        } catch (PaymentDeclinedException e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Payment failed. Please check your payment details and try again."
            );
            return "redirect:/checkout";
        } catch (Exception e) {
            // e.g. an empty cart.
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
        // Post/Redirect/Get: redirect to a real success URL so a refresh can't re-submit the order.
        return "redirect:/checkout/success/" + order.orderNumber();
    }

    /** Order confirmation page. Reuses the user-scoped lookup, so only the buyer can view their receipt. */
    @GetMapping("/success/{orderNumber}")
    public String checkoutSuccess(@PathVariable String orderNumber, Model model) {
        model.addAttribute("finalOrderDetails",
                orderService.getOrderByOrderNumber(orderNumber, authService.getCurrentUser()));
        return "checkoutSuccess";
    }
}
