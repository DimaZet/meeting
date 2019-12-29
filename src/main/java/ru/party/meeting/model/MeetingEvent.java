package ru.party.meeting.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MeetingEvent {

    @Id
    @Getter
    private final UUID id;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter //@CreatedDate TODO auditor
    private Instant createdAt;

    public MeetingEvent(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
