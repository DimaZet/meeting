package ru.party.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEventRequest {
    private String title;
    private String description;
}
