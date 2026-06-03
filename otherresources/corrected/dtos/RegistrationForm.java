package edu.hield.security.dtos;

import java.util.List;

/**
 * Form-backing object for the registration POST. Same rationale as
 * {@link ProfileUpdateForm}: bind a DTO, not the entity.
 */
public record RegistrationForm(
        String username,
        String firstName,
        String lastName,
        String email,
        String password,
        List<String> roleNames
) {}
