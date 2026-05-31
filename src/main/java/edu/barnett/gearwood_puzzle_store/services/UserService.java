package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.UserDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.utils.CurrentUserContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {

    @PreAuthorize("isAuthenticated()")
    void changePassword(String currentPassword, String newPassword);

    @PreAuthorize("isAuthenticated()")
    void prepareDashboardModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareProfileModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareSettingsModel(Model model);

    UserDto findUserByEmail(String email);

    List<User> getAllUsers();

    UserDto registerNewUser(User user);


    CurrentUserContext getCurrentUserContext();

    @PreAuthorize("isAuthenticated()")
    UserDto updateUser(User updateUser, String password);
}
