package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Exposes the current user's display data to every view (notably the header
 * fragment) on each request. Derived from the SecurityContext — which the JWT
 * filter rebuilds from the cookie per request — rather than the HttpSession,
 * so the values stay correct across server restarts.
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
     * True only for a genuinely logged-in user. Anonymous requests carry an
     * AnonymousAuthenticationToken whose isAuthenticated() returns true, so we
     * exclude it explicitly — otherwise getCurrentUser() would look up the
     * "anonymousUser" principal and throw.
     */
    private boolean isLoggedIn(Authentication auth) {
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }
}
