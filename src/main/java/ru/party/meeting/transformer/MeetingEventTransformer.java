package ru.party.meeting.transformer;

import ru.party.meeting.dto.MeetingEventTO;
import ru.party.meeting.model.MeetingEvent;

public class MeetingEventTransformer {

    public MeetingEventTO transform(MeetingEvent event) {
        return new MeetingEventTO(
                event.getId(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getCreatedBy(),
                event.getLastModifiedBy(),
                event.getTitle(),
                event.getDescription());
    }
}
