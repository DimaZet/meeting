package ru.party.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.party.gateway.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
