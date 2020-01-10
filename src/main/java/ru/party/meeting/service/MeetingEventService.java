package ru.party.meeting.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.party.meeting.exception.NotFoundException;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.model.Status;
import ru.party.meeting.repository.MeetingEventRepository;

@Slf4j
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

    public MeetingEvent findEventById(UUID eventId) throws NotFoundException {
        return meetingEventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
    }

    public List<MeetingEvent> findAllEvents() {
        return meetingEventRepository.findAll();
    }

    public MeetingEvent updateEvent(UUID eventId, @Nullable String title, @Nullable String description)
            throws NotFoundException {
        MeetingEvent event = meetingEventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
        if (Objects.nonNull(title)) {
            event.setTitle(title);
        }
        if (Objects.nonNull(description)) {
            event.setDescription(description);
        }
        return meetingEventRepository.save(event);
    }

    public void deleteEventById(UUID id) throws NotFoundException {
        MeetingEvent deletedEvent = meetingEventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (deletedEvent.getStatus().equals(Status.DELETED)) {
            log.info("In deleteEventById: event with id {} is already deleted", id);
            return;
        }
        deletedEvent.setStatus(Status.DELETED);
        meetingEventRepository.save(deletedEvent);
    }
}
