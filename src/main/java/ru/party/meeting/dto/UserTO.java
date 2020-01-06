package ru.party.meeting.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTO {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<RoleTO> roles;
}
