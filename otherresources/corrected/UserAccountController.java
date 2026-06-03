package edu.hield.security.controllers;

import edu.hield.security.dtos.ProfileUpdateForm;
import edu.hield.security.dtos.RegistrationForm;
import edu.hield.security.dtos.SettingsView;
import edu.hield.security.exceptions.EmailAlreadyExistsException;
import edu.hield.security.services.AuthService;
import edu.hield.security.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Corrected controller. Its job is strictly HTTP plumbing:
 *   - bind request params / form DTOs
 *   - call ONE service method
 *   - choose a view or redirect, set flash messages
 *
 * It no longer: copies entity fields, decides business rules, makes multiple
 * service/save calls for one action, or hands a Model to the service.
 */
@Controller
public class UserAccountController {

    private final AuthService authService;
    private final UserService userService;

    public UserAccountController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public String showIndex() {
        return "index";
    }

    // === LOGIN ===
    @GetMapping("/login")
    public String showLoginForm() {
        // No need to seed an empty User; the form posts plain credentials.
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletResponse response,
                               Model model) {
        try {
            // AuthService exposes a credentials-based method rather than taking a User entity.
            response.addCookie(authService.loginAndCreateJwtCookie(username, password));
            return "redirect:/dashboard";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout(HttpServletResponse response) {
        authService.clearJwtCookie(response);
        return "redirect:/login";
    }

    // === DASHBOARD / PROFILE / SETTINGS ===
    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String showDashboard(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String showProfile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile";
    }

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String showSettings(Model model) {
        // service returns data; the controller is the only thing that touches Model
        SettingsView view = userService.getSettings();
        model.addAttribute("user", view.user());
        model.addAttribute("isManager", view.manager());
        model.addAttribute("currentEmployees", view.currentEmployees());
        model.addAttribute("availableUsers", view.availableUsers());
        return "account_settings";
    }

    @PostMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String updateSettings(@ModelAttribute ProfileUpdateForm form,
                                 @RequestParam(required = false) List<Long> addIds,
                                 @RequestParam(required = false) List<Long> removeIds,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        // each call is one whole business action; no field copying here
        userService.updateProfile(form, file);
        if (addIds != null || removeIds != null) {
            userService.reassignTeam(addIds, removeIds);   // service enforces manager-only
        }
        redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        return "redirect:/settings";
    }

    // === ADMIN + MANAGER VIEWS ===
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all_users";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/team")
    public String showMyTeam(Model model) {
        model.addAttribute("team", userService.getTeamForCurrentManager());
        return "my_team";
    }

    // === REGISTRATION ===
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationForm form,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.register(form, file);   // one call does it all (roles, hash, picture, save)
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";
        } catch (EmailAlreadyExistsException e) {
            // catch the SPECIFIC, expected case; its message is intentionally user-safe.
            // Unexpected exceptions are left to a @ControllerAdvice handler (no stack-trace leak).
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    // === PROFILE PICTURE: serving the file is legitimately a web concern ===
    @GetMapping("/profile-pictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveProfilePicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/profile-pictures/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaTypeFactory.getMediaType(resource)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
