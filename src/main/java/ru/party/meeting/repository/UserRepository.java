package ru.party.meeting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.party.meeting.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);
}