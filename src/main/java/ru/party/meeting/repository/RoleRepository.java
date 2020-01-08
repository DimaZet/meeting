package ru.party.meeting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.party.meeting.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}