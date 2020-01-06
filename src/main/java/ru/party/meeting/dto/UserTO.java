package ru.party.meeting.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTO {
    private long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String firstName;
    private String lastName;
    private List<RoleTO> roles;

    @JsonCreator
    public UserTO(
            @JsonProperty("id") long id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("username") String username,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("roles") List<RoleTO> roles) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
