package ru.party.meeting.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class MeetingEvent {

    @Id
    @Setter(AccessLevel.NONE)
    private final UUID id;
    private String title;
    private String description;
    //@CreatedDate TODO auditor
    private Instant createdAt;

    public MeetingEvent(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
