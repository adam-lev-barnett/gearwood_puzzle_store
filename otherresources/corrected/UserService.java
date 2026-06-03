package edu.hield.security.services;

import edu.hield.security.dtos.ProfileUpdateForm;
import edu.hield.security.dtos.RegistrationForm;
import edu.hield.security.dtos.SettingsView;
import edu.hield.security.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Corrected service contract.
 *
 * Note what is NOT here anymore: any method that takes a {@code Model}. The service
 * returns data; the controller decides how to expose it to the view. Every method
 * returns a value or performs a single, well-defined business action.
 */
public interface UserService {

    /** The authenticated user, resolved from the security context. */
    User getCurrentUser();

    /** All data the settings view needs, as a plain object (no Model). */
    SettingsView getSettings();

    /** Apply profile changes (name/email/optional password/optional picture) and save once. */
    void updateProfile(ProfileUpdateForm form, MultipartFile picture);

    /** Manager-only: attach/detach employees. Authorization is enforced inside. */
    void reassignTeam(List<Long> addIds, List<Long> removeIds);

    /** Create a new user (encodes password, assigns roles, optional picture) and save once. */
    User register(RegistrationForm form, MultipartFile picture);

    List<User> getAllUsers();

    List<User> getTeamForCurrentManager();
}
