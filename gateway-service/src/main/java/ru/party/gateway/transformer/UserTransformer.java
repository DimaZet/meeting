package ru.party.gateway.transformer;

import java.util.stream.Collectors;

import ru.party.gateway.dto.RoleTO;
import ru.party.gateway.dto.UserTO;
import ru.party.gateway.model.Role;
import ru.party.gateway.model.User;

public class UserTransformer {

    public UserTO transform(User user) {
        return new UserTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream()
                        .map(this::transform)
                        .collect(Collectors.toList()));
    }

    public RoleTO transform(Role role) {
        return new RoleTO(
                role.getId(),
                role.getName());
    }
}
