package ru.party.meetingservice.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.party.meetingservice.model.MeetingEvent;

public interface MeetingEventRepository extends MongoRepository<MeetingEvent, UUID> {
}
