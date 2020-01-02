package ru.party.meeting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.party.meeting.dto.CreateUserRequest;
import ru.party.meeting.exception.UserAlreadyRegisteredException;
import ru.party.meeting.model.User;
import ru.party.meeting.service.RoleService;
import ru.party.meeting.service.UserService;
import ru.party.meeting.transformer.UserTransformer;

@Controller
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserTransformer userTransformer = new UserTransformer();

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/users/register")
    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getLogin(),
                createUserRequest.getPassword(),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName(),
                List.of(roleService.findByName("USER")));
        try {
            return ResponseEntity.ok(
                    userTransformer.transform(userService.register(user)));
        } catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

