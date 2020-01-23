package ru.party.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.party.gateway.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
