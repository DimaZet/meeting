package ru.party.meeting.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.party.meeting.model.MeetingEvent;

@Repository
public interface MeetingEventRepository extends MongoRepository<MeetingEvent, UUID> {
}
