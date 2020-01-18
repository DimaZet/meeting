package ru.party.searchservice.transformer;

import java.time.Instant;

import ru.party.searchservice.dto.MeetingEventTO;
import ru.party.searchservice.dto.StatusTO;
import ru.party.searchservice.model.MeetingEvent;
import ru.party.searchservice.model.Status;

public class MeetingEventTransformer {

    public MeetingEventTO transform(MeetingEvent event) {
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
