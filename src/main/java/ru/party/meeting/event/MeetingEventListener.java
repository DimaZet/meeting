package ru.party.meeting.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.party.meeting.model.ElasticMeetingEvent;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.repository.MeetingEventElasticRepository;

@Component
public class MeetingEventListener extends AbstractMongoEventListener<MeetingEvent> {

    private final MeetingEventElasticRepository meetingEventElasticRepository;

    @Autowired
    public MeetingEventListener(MeetingEventElasticRepository meetingEventElasticRepository) {
        this.meetingEventElasticRepository = meetingEventElasticRepository;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<MeetingEvent> event) {
        MeetingEvent meetingEvent = event.getSource();
        meetingEventElasticRepository.save(
                new ElasticMeetingEvent(
                        meetingEvent.getId(),
                        meetingEvent.getTitle(),
                        meetingEvent.getDescription(),
                        meetingEvent.getCreatedAt().toString(),
                        meetingEvent.getUpdatedAt().toString(),
                        meetingEvent.getCreatedBy(),
                        meetingEvent.getLastModifiedBy(),
                        meetingEvent.getStatus()
                )
        );
        super.onAfterSave(event);
    }
}
