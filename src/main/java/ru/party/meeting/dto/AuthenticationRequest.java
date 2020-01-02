package ru.party.meeting.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    private final String login;
    private final String password;
}
