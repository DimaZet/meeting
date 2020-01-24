package ru.party.meetingservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.party.meetingservice.model.MeetingEvent;
import ru.party.meetingservice.service.MessagePublisher;
import ru.party.meetingservice.transformer.MeetingEventTransformer;

@Slf4j
@Component
public class MeetingEventListener extends AbstractMongoEventListener<MeetingEvent> {

    private final MeetingEventTransformer transformer = new MeetingEventTransformer();
    private final MessagePublisher publisher;

    @Autowired
    public MeetingEventListener(MessagePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<MeetingEvent> event) {
        publisher.publish(
                transformer.transform(event.getSource()));
        super.onAfterSave(event);
    }
}
