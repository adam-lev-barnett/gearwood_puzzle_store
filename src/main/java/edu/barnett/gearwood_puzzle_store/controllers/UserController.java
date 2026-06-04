package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.entities.User;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String showProfile(Authentication auth, Model model) {
        // Controller assembles the view model from the security context; the
        // service just returns data (a UserDto). auth.getName() is the email.
        model.addAttribute("user", userService.findUserByEmail(auth.getName()));
        return "account";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editAccount(@ModelAttribute("user") User updatedUser,
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


}
