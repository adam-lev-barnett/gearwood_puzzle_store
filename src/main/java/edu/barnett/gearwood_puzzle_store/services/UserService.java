package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.RegistrationRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.UserDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;


public interface UserService {

    void registerUser(RegistrationRequestDto registrationRequest);

    @PreAuthorize("isAuthenticated()")
    void changePassword(String currentPassword, String newPassword);

    void loginUser(LoginRequestDto request,
                   HttpServletResponse response);

    UserDto findUserByEmail(String email);

    void registerNewUser(User user);

    @PreAuthorize("isAuthenticated()")
    void updateUser(User updateUser, String password);

    int getUserCartSize();


}
