package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.RegistrationRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.UserDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;


public interface UserService {

    void registerUser(RegistrationRequestDto registrationRequest);

    void loginUser(LoginRequestDto request,
                   HttpServletResponse response);

    UserDto findUserByEmail(String email);

    @PreAuthorize("isAuthenticated()")
    void updateUserInfo(User updateUser, String password);

    @PreAuthorize("isAuthenticated()")
    void updatePassword(String currentPassword, String newPassword, String confirmPassword);

    int getUserCartSize();


}
