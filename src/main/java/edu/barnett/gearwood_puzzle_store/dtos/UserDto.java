package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Role;
import edu.barnett.gearwood_puzzle_store.entities.User;

import java.util.List;
import java.util.Set;

public record UserDto(
        String email,
        String firstName,
        String lastName,
        Set<Role> roles
) {
    public UserDto(User user) {
        this(
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRoles()
        );
    }
}
