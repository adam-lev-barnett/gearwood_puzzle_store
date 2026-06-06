package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;
import edu.barnett.gearwood_puzzle_store.payment.model.PaymentRequest;
import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final AuthService authService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, AuthService authService) {
        this.checkoutService = checkoutService;
        this.authService = authService;
    }

    //TODO
    @PostMapping
    public String checkout(@ModelAttribute ShippingInfo shippingInfo, @ModelAttribute PaymentRequest paymentRequest, Model model) {
        checkoutService.processCheckout(authService.getCurrentUser(), shippingInfo, paymentRequest);
        return "/home";
    }
}
