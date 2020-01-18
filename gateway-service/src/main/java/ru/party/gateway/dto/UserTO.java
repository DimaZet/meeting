package ru.party.gateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<RoleTO> roles;

    @JsonCreator
    public UserTO(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("roles") List<RoleTO> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
