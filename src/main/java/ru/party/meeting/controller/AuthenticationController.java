package ru.party.meeting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.party.meeting.dto.LoginUserRequest;
import ru.party.meeting.exception.NotFoundException;
import ru.party.meeting.model.User;
import ru.party.meeting.security.JwtTokenUtil;
import ru.party.meeting.service.UserService;

@Controller
@RequestMapping("/tokens")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity loginAndGetToken(@RequestBody LoginUserRequest request)
            throws AuthenticationException, NotFoundException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.findByUsername(request.getUsername());
        final String token = jwtTokenUtil.createToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok((token));
    }
}
