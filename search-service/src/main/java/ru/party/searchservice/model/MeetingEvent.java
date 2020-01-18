package ru.party.searchservice.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "meeting", type = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEvent {

    @Id
    UUID id;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String lastModifiedBy;
    private Status status;
}