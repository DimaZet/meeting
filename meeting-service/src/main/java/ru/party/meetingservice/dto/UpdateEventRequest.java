package ru.party.meetingservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEventRequest {
    private UUID id;
    private String title;
    private String description;
}
