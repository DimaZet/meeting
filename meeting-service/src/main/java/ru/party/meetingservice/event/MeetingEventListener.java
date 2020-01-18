package ru.party.meetingservice.event;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.party.meetingservice.model.MeetingEvent;

@Component
public class MeetingEventListener extends AbstractMongoEventListener<MeetingEvent> {

    @Override
    public void onAfterSave(AfterSaveEvent<MeetingEvent> event) {
        MeetingEvent meetingEvent = event.getSource();
        //TODO send meetingEvent in elasticsearch
        super.onAfterSave(event);
    }
}
