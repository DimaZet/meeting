package ru.party.meeting.transformer;

import java.util.stream.Collectors;

import ru.party.meeting.dto.RoleTO;
import ru.party.meeting.dto.UserTO;
import ru.party.meeting.model.Role;
import ru.party.meeting.model.User;

public class UserTransformer {

    public UserTO transform(User user) {
        return new UserTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
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
                role.getCreatedAt(),
                role.getUpdatedAt(),
                role.getName());
    }
}
