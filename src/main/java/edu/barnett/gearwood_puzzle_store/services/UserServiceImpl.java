package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.UserDto;
import edu.barnett.gearwood_puzzle_store.entities.Role;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.repositories.RoleRepository;
import edu.barnett.gearwood_puzzle_store.repositories.UserRepository;
import edu.barnett.gearwood_puzzle_store.utils.CurrentUserContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CurrentUserContext getCurrentUserContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new CurrentUserContext(user, auth);
    }


    @Override
    public UserDto findUserByEmail(String email) {
        return new UserDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        User currentUser = this.getCurrentUserContext().user();
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @Override
    public void prepareDashboardModel(Model model) {
        CurrentUserContext context = getCurrentUserContext();
        model.addAttribute("user", context.user());
        model.addAttribute("authorization", context.auth());
    }

    @Override
    public void prepareProfileModel(Model model) {
        model.addAttribute("user", getCurrentUserContext().user());
    }

    @Override
    public void prepareSettingsModel(Model model) {

    }

    @Override
    public UserDto registerNewUser(User user) {
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));
        user.addRole(customerRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser =  userRepository.save(user);
        return new UserDto(newUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto updateUser(User updateUser, String password) {
        User currentUser = this.getCurrentUserContext().user();
        if (!currentUser.getPassword().equals(password)) throw new BadCredentialsException("Could not update user information. Wrong password");
        // Last name or first name may be null or empty if user is only updating one. So no error thrown.
        if (updateUser.getFirstName() != null && !updateUser.getFirstName().isEmpty()) currentUser.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null && !updateUser.getLastName().isEmpty()) currentUser.setLastName(updateUser.getLastName());
        User updatedUser = userRepository.save(currentUser);
        return new UserDto(updatedUser);
    }

}
