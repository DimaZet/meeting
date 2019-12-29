package ru.party.meeting.event;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.party.meeting.model.MeetingEvent;

@Component
public class MongoEventListener extends AbstractMongoEventListener<MeetingEvent> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<MeetingEvent> event) {
        event.getSource().setCreatedAt(Instant.now());
        super.onBeforeSave(event);
    }
}
