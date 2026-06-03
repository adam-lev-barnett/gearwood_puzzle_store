package edu.hield.security.dtos;

/**
 * Form-backing object for the "update settings/profile" POST.
 *
 * Why a DTO instead of binding the {@code User} entity directly:
 *  - Prevents over-posting: the user can only set the fields listed here, not
 *    arbitrary entity fields (roles, id, password hash, manager, etc.).
 *  - Decouples the HTML form from the persistence model.
 *
 * Spring 6 / Boot 3 binds records as form objects via their canonical constructor,
 * so {@code @ModelAttribute ProfileUpdateForm form} works out of the box.
 */
public record ProfileUpdateForm(
        String firstName,
        String lastName,
        String email,
        String password   // optional: blank/null means "leave password unchanged"
) {}
