package ru.party.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.party.gateway.dto.CreateUserRequest;
import ru.party.gateway.dto.UserTO;
import ru.party.gateway.exception.NotFoundException;
import ru.party.gateway.exception.UserAlreadyRegisteredException;
import ru.party.gateway.model.User;
import ru.party.gateway.service.UserService;
import ru.party.gateway.transformer.UserTransformer;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer = new UserTransformer();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest)
            throws NotFoundException, UserAlreadyRegisteredException {
        User user = new User(createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName());
        return ResponseEntity.ok(
                userTransformer.transform(userService.registerWithDefaultRole(user)));
    }

    @GetMapping
    public ResponseEntity<List<UserTO>> getAll() {
        return ResponseEntity.ok(
                userService.getAll().stream()
                        .map(userTransformer::transform)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserTO> findById(@PathVariable(name = "userId") Long userId)
            throws NotFoundException {
        return ResponseEntity.ok(
                userTransformer.transform(userService.findById(userId)));
    }

}
