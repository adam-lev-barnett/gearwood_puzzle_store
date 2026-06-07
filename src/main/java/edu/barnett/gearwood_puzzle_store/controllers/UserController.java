package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.services.AuthService;
import edu.barnett.gearwood_puzzle_store.services.OrderService;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, OrderService orderService, AuthService authService) {
        this.userService = userService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String showProfile(Authentication auth, Model model) {
        // Controller assembles the view model from the security context; the
        // service just returns data (a UserDto). auth.getName() is the email.
        model.addAttribute("user", userService.findUserByEmail(auth.getName()));
        model.addAttribute("orders", orderService.getOrdersByUser(authService.getCurrentUser()));
        return "account";
    }

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public String orderHistory(Model model) {
        model.addAttribute("orders", orderService.getOrdersByUser(authService.getCurrentUser()));
        return "orderHistory";
    }

    @GetMapping("/orders/{orderNumber}")
    @PreAuthorize("isAuthenticated()")
    public String orderDetails(@PathVariable String orderNumber, Model model) {
        // Scoped to the current user, so a customer can only view their own order.
        model.addAttribute("order", orderService.getOrderByOrderNumber(orderNumber, authService.getCurrentUser()));
        return "orderDetails";
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String showEditForm(Authentication auth, Model model) {
        // Same UserDto the account page uses — prefills firstName/lastName/email in the form.
        model.addAttribute("user", userService.findUserByEmail(auth.getName()));
        return "editAccount";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editAccount(@ModelAttribute("user") User updatedUser,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            userService.updateUserInfo(updatedUser, password);
            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update account: " + ex.getMessage());
        }
        return "redirect:/account";
    }

    @GetMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public String showChangePasswordForm() {
        // No model data — password fields are always entered fresh, never prefilled.
        return "changePassword";
    }

    @PostMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        try {
            userService.updatePassword(oldPassword, newPassword, confirmPassword);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/account/password";
        }
        return "redirect:/account";
    }



}
