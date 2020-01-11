package ru.party.meeting.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meeting.dto.MeetingEventTO;
import ru.party.meeting.repository.MeetingEventElasticRepository;
import ru.party.meeting.transformer.MeetingEventTransformer;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Controller
@RequestMapping("/api/events/find")
public class FindMeetingEventController {

    private final MeetingEventElasticRepository meetingEventElasticRepository;
    private final MeetingEventTransformer meetingEventTransformer = new MeetingEventTransformer();

    @Autowired
    public FindMeetingEventController(MeetingEventElasticRepository meetingEventElasticRepository) {
        this.meetingEventElasticRepository = meetingEventElasticRepository;
    }

    @GetMapping
    public ResponseEntity<List<MeetingEventTO>> findByQuery(
            @RequestParam(name = "q") String query) {
        return ResponseEntity.ok(
                meetingEventElasticRepository.search(queryStringQuery(query)).stream()
                        .map(meetingEventTransformer::transform)
                        .collect(Collectors.toList()));
    }

}
