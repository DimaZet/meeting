package ru.party.meeting.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleTO {
    private long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
}
