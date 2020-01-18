package ru.party.meetingservice.transformer;

import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.dto.StatusTO;
import ru.party.meetingservice.model.MeetingEvent;
import ru.party.meetingservice.model.Status;

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
