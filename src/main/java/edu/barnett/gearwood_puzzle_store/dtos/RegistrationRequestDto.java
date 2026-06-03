package edu.barnett.gearwood_puzzle_store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrationRequestDto(
        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,

        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="Incorrectly formatted email address")
        String email,

        @NotBlank(message = "Please enter a password")
        String password,

        @NotBlank(message = "Please confirm your password")
        String confirmPassword) {
}
