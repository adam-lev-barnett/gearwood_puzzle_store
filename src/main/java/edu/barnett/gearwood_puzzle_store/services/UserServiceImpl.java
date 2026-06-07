package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.RegistrationRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.UserDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.exceptions.AlreadyExistsException;
import edu.barnett.gearwood_puzzle_store.exceptions.BadParameterException;
import edu.barnett.gearwood_puzzle_store.exceptions.NotFoundException;
import edu.barnett.gearwood_puzzle_store.repositories.RoleRepository;
import edu.barnett.gearwood_puzzle_store.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final CartService cartService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthService authService, CartService cartService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public void registerUser(RegistrationRequestDto request) {

        // Basic field check already handled on DTO level via annotations

        if (userRepository.existsByEmail(request.email()))
            throw new AlreadyExistsException("User with email " + request.email() + " already exists");

        // Passwords must match
        if (!request.password().equals(request.confirmPassword()))
            throw new BadParameterException("Passwords do not match");

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        // Automatically assign them the customer role. They shouldn't be able to choose their roles
        user.addRole(roleRepository.findByName("CUSTOMER").orElseThrow(
                () -> new NotFoundException("CUSTOMER role not found")
        ));

        userRepository.save(user);
    }

    @Override
    public void loginUser(LoginRequestDto request,
                          HttpServletResponse response) {
        Cookie jwtCookie = authService.loginAndCreateJwtCookie(request);
        response.addCookie(jwtCookie);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return new UserDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }

    @Override
    @Transactional
    public void updateUserInfo(User updateUser, String password) {
        User currentUser = authService.getCurrentUser();
        if (!passwordEncoder.matches(password, currentUser.getPassword())) throw new BadCredentialsException("Could not update user information. Wrong password");
        // Last name or first name may be null or empty if user is only updating one. So no error thrown.
        if (updateUser.getFirstName() != null && !updateUser.getFirstName().isEmpty()) currentUser.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null && !updateUser.getLastName().isEmpty()) currentUser.setLastName(updateUser.getLastName());
        userRepository.save(currentUser);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @Override
    public void updatePassword(String currentPassword, String newPassword, String  confirmPassword) {
        if (newPassword.length() < 6) throw new BadParameterException("Password must be at least 6 characters");
        User currentUser = authService.getCurrentUser();
        if (passwordEncoder.matches(currentPassword, newPassword)) throw new BadParameterException("New password must be different from current password");
        if (!currentUser.getPassword().equals(currentPassword)) throw new BadParameterException("Invalid current password");
        if (!newPassword.equals(confirmPassword)) throw new BadParameterException("New passwords do not match");
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @Override
    public int getUserCartSize() {
        return cartService.getCartItems().size();
    }



}
