package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String email, String password);
    User findByEmail(String email);
    void updateProfile(User user, String firstName, String lastName);
    void changePassword(User user, String currentPassword, String newPassword);

    @PreAuthorize("isAuthenticated()")
    void prepareDashboardModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareProfileModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareSettingsModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void updateUserSettings(User updatedUser, String password, List<Long> addIds, List<Long> removeIds);

    List<User> getAllUsers();

    User registerNewUser(User user, List<String> roleNames);

    void updateUser(User savedUser);

    User getCurrentUser();
}
