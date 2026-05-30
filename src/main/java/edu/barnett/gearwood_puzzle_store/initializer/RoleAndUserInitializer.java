package edu.barnett.gearwood_puzzle_store.initializer;

import edu.barnett.gearwood_puzzle_store.entities.Role;
import edu.barnett.gearwood_puzzle_store.entities.User;
import edu.barnett.gearwood_puzzle_store.repositories.RoleRepository;
import edu.barnett.gearwood_puzzle_store.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RoleAndUserInitializer {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RoleAndUserInitializer(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (roleRepo.count() != 0 || userRepo.count() != 0) {
            System.out.println("Role/user data already present - not executing initializer");
            return;
        }
        System.out.println("Initializing roles and admin user");

        Role customerRole = roleRepo.save(new Role("CUSTOMER"));
        Role adminRole    = roleRepo.save(new Role("ADMIN"));

        User admin = new User("Admin", "User", "admin.user@gmail.com", passwordEncoder.encode("Admin1234"));
        admin.addRole(adminRole);
        userRepo.save(admin);
    }
}
