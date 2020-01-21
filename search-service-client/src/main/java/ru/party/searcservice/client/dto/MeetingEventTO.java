package ru.party.searcservice.client.dto;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingEventTO {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String lastModifiedBy;
    private String title;
    private String description;
    private StatusTO status;

    @JsonCreator
    public MeetingEventTO(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("createdBy") String createdBy,
            @JsonProperty("lastModifiedBy") String lastModifiedBy,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("status") StatusTO status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
