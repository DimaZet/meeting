package ru.party.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.party.meeting.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}