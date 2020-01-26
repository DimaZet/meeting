package ru.party.meetingservice.service;

import java.util.List;
import java.util.UUID;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.party.meetingservice.exception.NotFoundException;
import ru.party.meetingservice.exception.UserPrivilegesException;
import ru.party.meetingservice.model.MeetingEvent;
import ru.party.meetingservice.model.Status;
import ru.party.meetingservice.repository.MeetingEventRepository;

@Slf4j
@Service
public class MeetingEventService {

    private final MeetingEventRepository meetingEventRepository;

    public MeetingEventService(MeetingEventRepository meetingEventRepository) {
        this.meetingEventRepository = meetingEventRepository;
    }

    public MeetingEvent createEvent(@NonNull String username,
                                    String title, String description) {
        UUID id = UUID.randomUUID();
        MeetingEvent event = new MeetingEvent(id, title, description);
        event.setCreatedBy(username);
        event.setLastModifiedBy(username);
        return meetingEventRepository.save(event);
    }

    public MeetingEvent findEventById(UUID eventId) throws NotFoundException {
        return meetingEventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
    }

    public List<MeetingEvent> findAllEvents() {
        return meetingEventRepository.findAll();
    }

    public MeetingEvent updateEvent(@NonNull String username, UUID eventId,
                                    @Nullable String title, @Nullable String description)
            throws NotFoundException, UserPrivilegesException {
        MeetingEvent event = meetingEventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
        assertPrivileges(event, username);
        if (!StringUtil.isNullOrEmpty(title)) {
            event.setTitle(title);
        }
        if (!StringUtil.isNullOrEmpty(description)) {
            event.setDescription(description);
        }
        event.setLastModifiedBy(username);
        return meetingEventRepository.save(event);
    }

    public void deleteEventById(String username, UUID id) throws NotFoundException, UserPrivilegesException {
        MeetingEvent deletedEvent = meetingEventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        assertPrivileges(deletedEvent, username);
        if (deletedEvent.getStatus().equals(Status.DELETED)) {
            log.info("In deleteEventById: event with id {} is already deleted", id);
            return;
        }
        deletedEvent.setStatus(Status.DELETED);
        meetingEventRepository.save(deletedEvent);
    }

    private void assertPrivileges(MeetingEvent event, String username) throws UserPrivilegesException {
        if (!event.getCreatedBy().equals(username)) {
            throw new UserPrivilegesException();
        }
    }
}
