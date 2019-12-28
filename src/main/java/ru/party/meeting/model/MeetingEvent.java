package ru.party.meeting.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class MeetingEvent {

    @Id
    private UUID id;
    private String title;
    private String description;
    @CreatedDate
    private Instant createdAt;

    public MeetingEvent(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}