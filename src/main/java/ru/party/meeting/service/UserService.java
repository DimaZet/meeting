package ru.party.meeting.service;

import java.time.Instant;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.party.meeting.exception.NotFoundException;
import ru.party.meeting.exception.UserAlreadyRegisteredException;
import ru.party.meeting.model.Status;
import ru.party.meeting.model.User;
import ru.party.meeting.repository.RoleRepository;
import ru.party.meeting.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerWithDefaultRole(User user) throws UserAlreadyRegisteredException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.info("In register - user with username: {} already registered", user.getUsername());
            throw new UserAlreadyRegisteredException();
        }
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));
        user.setRoles(
                List.of(roleRepository.findByName("ROLE_USER")));
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(Instant.now()); //TODO
        user.setUpdatedAt(Instant.now()); //TODO
        User registeredUser = userRepository.save(user);
        log.info("In register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        log.info("In getAll - {} users found", users.size());
        return users;
    }

    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public User delete(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        user.setStatus(Status.DELETED);
        log.info("In delete - user: {} succefully deleted", user);
        return userRepository.save(user);
    }

    public User ban(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        user.setStatus(Status.BANNED);
        log.info("In ban - user: {} succefully banned", user);
        return userRepository.save(user);
    }
}