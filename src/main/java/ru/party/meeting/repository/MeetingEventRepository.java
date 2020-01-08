package ru.party.meeting.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.party.meeting.model.MeetingEvent;

public interface MeetingEventRepository extends MongoRepository<MeetingEvent, UUID> {
}
