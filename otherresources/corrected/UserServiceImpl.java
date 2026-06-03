package edu.hield.security.services;

import edu.hield.security.dtos.ProfileUpdateForm;
import edu.hield.security.dtos.RegistrationForm;
import edu.hield.security.dtos.SettingsView;
import edu.hield.security.entities.Role;
import edu.hield.security.entities.User;
import edu.hield.security.exceptions.EmailAlreadyExistsException;     // illustrative custom exceptions
import edu.hield.security.exceptions.ProfilePictureStorageException;
import edu.hield.security.repositories.RoleRepository;
import edu.hield.security.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Corrected service. Key changes vs. the original:
 *  1. No {@code Model} anywhere — every method returns data or performs one action.
 *  2. One current-user lookup ({@link #getCurrentUser()}); the duplicate
 *     getCurrentUserContext() is gone.
 *  3. Each write path saves the entity exactly once. The picture helper no longer
 *     saves the user, so callers can't accidentally double/triple-save.
 *  4. Business logic lives here in whole units (updateProfile, register, reassignTeam),
 *     not split half-in-the-controller.
 *  5. Specific exceptions instead of broad catch + printStackTrace + leaking messages.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public SettingsView getSettings() {
        User user = getCurrentUser();

        if (!hasRole("ROLE_MANAGER")) {
            return new SettingsView(user, false, List.of(), List.of());
        }

        List<User> currentEmployees = userRepository.findByManager(user);
        List<User> availableUsers = userRepository.findByManagerIsNull().stream()
                .filter(u -> !u.getUsername().equals(user.getUsername()))
                .toList();

        return new SettingsView(user, true, currentEmployees, availableUsers);
    }

    @Override
    @Transactional
    public void updateProfile(ProfileUpdateForm form, MultipartFile picture) {
        User user = getCurrentUser();

        user.setFirstName(form.firstName());
        user.setLastName(form.lastName());
        user.setEmail(form.email());

        if (form.password() != null && !form.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.password()));
        }

        if (picture != null && !picture.isEmpty()) {
            // helper returns a filename only; it does NOT save the user
            user.setProfilePicture(storeProfilePicture(picture, user.getProfilePicture()));
        }

        userRepository.save(user);   // <-- the one and only write
    }

    @Override
    @Transactional
    public void reassignTeam(List<Long> addIds, List<Long> removeIds) {
        User manager = getCurrentUser();
        if (!hasRole("ROLE_MANAGER")) {
            // authorization belongs in the layer that owns the action
            throw new AccessDeniedException("Only managers can reassign team members");
        }
        setManagerFor(addIds, manager);
        setManagerFor(removeIds, null);
    }

    @Override
    @Transactional
    public User register(RegistrationForm form, MultipartFile picture) {
        if (userRepository.findByUsername(form.username()).isPresent()) {
            // a specific, user-safe exception the controller can show verbatim
            throw new EmailAlreadyExistsException("That username is already taken.");
        }

        Set<Role> roles = form.roleNames().stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new NoSuchElementException("Role not found: " + name)))
                .collect(Collectors.toSet());

        User user = new User();
        user.setUsername(form.username());
        user.setFirstName(form.firstName());
        user.setLastName(form.lastName());
        user.setEmail(form.email());
        user.setPassword(passwordEncoder.encode(form.password()));
        user.setRoles(roles);

        if (picture != null && !picture.isEmpty()) {
            user.setProfilePicture(storeProfilePicture(picture, null));
        }

        return userRepository.save(user);   // single write; picture filename already set
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTeamForCurrentManager() {
        return userRepository.findByManager(getCurrentUser());
    }

    // ------------------------------------------------------------------
    // private helpers
    // ------------------------------------------------------------------

    private void setManagerFor(List<Long> employeeIds, User manager) {
        if (employeeIds == null) return;
        for (Long id : employeeIds) {
            User employee = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
            employee.setManager(manager);
            userRepository.save(employee);
        }
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    /**
     * Stores the uploaded file, deletes the previous one, and RETURNS the new filename.
     * Deliberately does NOT touch the User entity or the database — persistence is the
     * caller's responsibility, so there's exactly one save per business action.
     */
    private String storeProfilePicture(MultipartFile file, String previousFilename) {
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profile-pictures");
            Files.createDirectories(uploadPath);

            if (previousFilename != null && !previousFilename.equals("default.jpg")) {
                Files.deleteIfExists(uploadPath.resolve(previousFilename));
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(uploadPath.resolve(filename).toFile());
            return filename;

        } catch (IOException ex) {
            // wrap as a domain exception; let a global handler turn it into a friendly page
            throw new ProfilePictureStorageException("Could not store profile picture", ex);
        }
    }
}
