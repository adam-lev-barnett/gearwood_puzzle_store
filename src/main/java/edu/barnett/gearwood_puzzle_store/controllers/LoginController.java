package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.PendingAddToCart;
import edu.barnett.gearwood_puzzle_store.dtos.RegistrationRequestDto;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.CartService;
import edu.barnett.gearwood_puzzle_store.services.ProductService;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final AuthService authService;
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    public LoginController(AuthService authService, UserService userService, CartService cartService, ProductService productService) {
        this.authService = authService;
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // Logged-in users are bounced home with a message instead of seeing the login form
    @GetMapping("/login")
    public String loadLoginForm(Authentication auth, RedirectAttributes redirectAttributes) {
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") LoginRequestDto user,
                                HttpServletResponse response,
                                HttpSession session,
                                Authentication auth,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        // Reject a login attempt from someone already authenticated
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        try {
            userService.loginUser(user, response);

            // Allows anonymous user to add an item to cart and have that item added to cart after logging in
            PendingAddToCart pending = (PendingAddToCart) session.getAttribute("pendingCartAdd");
            if (pending != null) {
                session.removeAttribute("pendingCartAdd");
                cartService.addToCart(pending.productCode(), pending.quantity());
                redirectAttributes.addFlashAttribute("successMessage", "Item added to cart.");
                return "redirect:/cart";
            }
            return "redirect:/home";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Authentication auth, RedirectAttributes redirectAttributes) {
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequestDto registrationRequest,
                               BindingResult bindingResult,
                               Authentication auth,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        // Bean Validation failures (blank fields, bad email, password < 6 chars) — show the first one.
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "registration";
        }
        try {
            userService.registerUser(registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";
        } catch (Exception e) {
            // Service-level failures (duplicate email, password mismatch) reuse the same ${error} slot.
            model.addAttribute("error", e.getMessage());
            return "registration";
        }
    }

    /**
     * True only for a genuinely logged-in user. Spring uses an
     * AnonymousAuthenticationToken for not-logged-in requests, and its
     * isAuthenticated() returns true — so we must exclude it explicitly.
     */
    private boolean isLoggedIn(Authentication auth) {
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }
}
