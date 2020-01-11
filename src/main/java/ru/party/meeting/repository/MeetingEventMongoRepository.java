package ru.party.meeting.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.party.meeting.model.MeetingEvent;

public interface MeetingEventMongoRepository extends MongoRepository<MeetingEvent, UUID> {
}
