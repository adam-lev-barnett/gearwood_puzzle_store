package edu.barnett.gearwood_puzzle_store.utils;

import edu.barnett.gearwood_puzzle_store.entities.User;
import org.springframework.security.core.Authentication;

public record CurrentUserContext(User user, Authentication auth) {}
