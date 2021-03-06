package ru.party.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
