package ru.party.meeting.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class MeetingEvent {

    @Id
    @Field
    private UUID id;

    @Field
    private String title;

    @Field
    private String description;

    @CreatedDate
    @Field
    private Instant createdAt;

    @LastModifiedDate
    @Field
    private Instant updatedAt;

    @CreatedBy
    @Field
    private String createdBy;
  
    @LastModifiedBy
    @Field
    private String lastModifiedBy;
  
    @Version
    private long version;

    public MeetingEvent(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
