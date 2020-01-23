package ru.party.searchservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.party.searchservice.model.MeetingEvent;
import ru.party.searchservice.repository.MeetingEventRepository;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class MeetingEventService {

    private final MeetingEventRepository meetingEventRepository;

    @Autowired
    public MeetingEventService(MeetingEventRepository meetingEventRepository) {
        this.meetingEventRepository = meetingEventRepository;
    }

    public MeetingEvent index(MeetingEvent meetingEvent) {
        return meetingEventRepository.index(meetingEvent);
    }

    public List<MeetingEvent> searchByWord(String keyWord) {
        return meetingEventRepository.search(queryStringQuery(keyWord));
    }
}
