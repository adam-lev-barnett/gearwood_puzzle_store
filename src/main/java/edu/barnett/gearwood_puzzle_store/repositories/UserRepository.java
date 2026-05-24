package edu.barnett.gearwood_puzzle_store.repositories;

import edu.barnett.gearwood_puzzle_store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByOrderByLastNameAsc();
}
