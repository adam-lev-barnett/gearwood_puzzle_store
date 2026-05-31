package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/account")
public class LogInController {

    private final UserService userService;

    @Autowired
    public LogInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String showDashboard(Model model) {
        userService.prepareDashboardModel(model);
        return "dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String showProfile(Model model) {
        userService.prepareProfileModel(model);
        return "profile";
    }

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String showSettings(Model model) {
        userService.prepareSettingsModel(model);
        return "account_settings";
    }

    @PostMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String updateSettings(@ModelAttribute("user") User updatedUser,
                                 @RequestParam String password,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(updatedUser, password);
            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update account: " + ex.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all_users";
    }
}
