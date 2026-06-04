package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.CartService;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    private final AuthService authService;
    private final UserService userService;
    private final CartService cartService;

    public HomeController(AuthService authService, UserService userService, CartService cartService) {
        this.authService = authService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
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
            return "redirect:/home";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Logout is handled by Spring Security's LogoutFilter (see SecurityConfig):
    // GET /logout clears the jwt cookie + session and redirects to /login?logout.

    @GetMapping("/register")
    public String showRegisterForm(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        if (isLoggedIn(auth)) {
            redirectAttributes.addFlashAttribute("infoMessage", "You're already logged in!");
            return "redirect:/home";
        }
        try {
            userService.registerNewUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/register";
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
