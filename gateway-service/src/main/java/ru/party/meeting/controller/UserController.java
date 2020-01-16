package ru.party.meeting.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.party.meeting.dto.CreateUserRequest;
import ru.party.meeting.dto.UserTO;
import ru.party.meeting.exception.NotFoundException;
import ru.party.meeting.exception.UserAlreadyRegisteredException;
import ru.party.meeting.model.User;
import ru.party.meeting.service.UserService;
import ru.party.meeting.transformer.UserTransformer;

@Controller
@RequestMapping(path = "/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer = new UserTransformer();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName());
        try {
            return ResponseEntity.ok(
                    userTransformer.transform(userService.registerWithDefaultRole(user)));
        } catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.badRequest().body("Already registered");
        } catch (NotFoundException e) {
            log.error("In registerUser: USER_ROLE not found", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

