package ru.party.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
}
