package edu.barnett.gearwood_puzzle_store.service;

import edu.barnett.gearwood_puzzle_store.entities.User;

public interface UserService {
    User register(String firstName, String lastName, String email, String password);
    User findByEmail(String email);
    void updateProfile(User user, String firstName, String lastName);
    void changePassword(User user, String currentPassword, String newPassword);
}
