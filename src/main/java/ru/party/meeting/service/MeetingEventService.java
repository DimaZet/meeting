package ru.party.meeting.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.repository.MeetingEventRepository;

@Service
public class MeetingEventService {

    private final MeetingEventRepository meetingEventRepository;

    public MeetingEventService(MeetingEventRepository meetingEventRepository) {
        this.meetingEventRepository = meetingEventRepository;
    }

    public MeetingEvent createEvent(String title, String description) {
        UUID id = UUID.randomUUID();
        MeetingEvent event = new MeetingEvent(id, title, description);
        return meetingEventRepository.save(event);
    }

    public List<MeetingEvent> getAllEvents() {
        return meetingEventRepository.findAll();
    }
}
