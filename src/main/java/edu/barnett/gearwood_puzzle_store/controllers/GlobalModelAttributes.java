package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Exposes the current user's display data to every view so that information isn't lost upon app restart for fragments, which aren't specifically scoped to a controller
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final AuthService authService;
    private final UserService userService;

    public GlobalModelAttributes(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @ModelAttribute("firstName")
    public String firstName(Authentication auth) {
        if (!isLoggedIn(auth)) return null;
        return authService.getCurrentUser().getFirstName();
    }

    @ModelAttribute("cartCount")
    public Integer cartCount(Authentication auth) {
        if (!isLoggedIn(auth)) return null;
        return userService.getUserCartSize();
    }

    /**
     * True only for a genuinely logged-in user. Anonymous authentication tokens are created
     * for cart persistence after login
     */
    private boolean isLoggedIn(Authentication auth) {
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }
}
