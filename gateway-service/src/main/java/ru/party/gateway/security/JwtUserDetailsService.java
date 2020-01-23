package ru.party.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.party.gateway.exception.NotFoundException;
import ru.party.gateway.model.User;
import ru.party.gateway.service.UserService;

@Slf4j
@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.findByUsername(username);
            JwtUser jwtUser = JwtUser.fromUser(user);
            log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
            return jwtUser;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
    }
}
