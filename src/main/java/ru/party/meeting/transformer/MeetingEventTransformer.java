package ru.party.meeting.transformer;

import java.time.Instant;

import ru.party.meeting.dto.MeetingEventTO;
import ru.party.meeting.dto.StatusTO;
import ru.party.meeting.model.ElasticMeetingEvent;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.model.Status;

public class MeetingEventTransformer {

    public MeetingEventTO transform(MeetingEvent event) {
        return new MeetingEventTO(
                event.getId(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getCreatedBy(),
                event.getLastModifiedBy(),
                event.getTitle(),
                event.getDescription(),
                transform(event.getStatus()));
    }

    public MeetingEventTO transform(ElasticMeetingEvent event) {
        return new MeetingEventTO(
                event.getId(),
                Instant.parse(event.getCreatedAt()),
                Instant.parse(event.getUpdatedAt()),
                event.getCreatedBy(),
                event.getLastModifiedBy(),
                event.getTitle(),
                event.getDescription(),
                transform(event.getStatus()));
    }

    private StatusTO transform(Status status) {
        switch (status) {
            case ACTIVE:
                return StatusTO.ACTIVE;
            case BANNED:
                return StatusTO.BANNED;
            case DELETED:
                return StatusTO.DELETED;
            default:
                throw new IllegalArgumentException();
        }
    }
}
