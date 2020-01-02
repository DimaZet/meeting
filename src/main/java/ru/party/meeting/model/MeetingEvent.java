package ru.party.meeting.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class MeetingEvent {

    @Id
    @Getter
    private final UUID id;

    @Getter
    @Setter
    @Field
    private String title;

    @Getter
    @Setter
    @Field
    private String description;

    @Getter
    @Setter //@CreatedDate TODO auditor
    @Field
    private Instant createdAt;

    public MeetingEvent(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
