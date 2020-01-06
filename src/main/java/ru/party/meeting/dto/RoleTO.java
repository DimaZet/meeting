package ru.party.meeting.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleTO {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;

    @JsonCreator
    public RoleTO(
            @JsonProperty("id") Long id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("name") String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
    }
}
