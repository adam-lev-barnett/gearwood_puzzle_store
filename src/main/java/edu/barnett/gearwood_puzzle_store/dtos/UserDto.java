package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Role;
import edu.barnett.gearwood_puzzle_store.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDto(
        String email,
        String firstName,
        String lastName,
        Set<String> roleNames
) {
    public UserDto(User user) {
        this(
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
        );
    }
}
