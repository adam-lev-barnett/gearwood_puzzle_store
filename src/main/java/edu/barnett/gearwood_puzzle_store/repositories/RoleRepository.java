package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
