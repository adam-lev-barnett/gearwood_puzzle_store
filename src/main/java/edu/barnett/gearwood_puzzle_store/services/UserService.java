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
    void prepareProfileModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareOrderDetailsModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareCartModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareCheckoutModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareOrderSucceedsModel(Model model);

    void prepareLoginModel(Model model);

    void prepareRegisterModel(Model model);

    void prepareLogoutModel(Model model);

    UserDto findUserByEmail(String email);

    List<User> getAllUsers();

    void registerNewUser(User user);

    CurrentUserContext getCurrentUserContext();

    @PreAuthorize("isAuthenticated()")
    void updateUser(User updateUser, String password);
}
