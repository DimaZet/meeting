package ru.party.searchservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.searchservice.dto.MeetingEventTO;
import ru.party.searchservice.repository.MeetingEventRepository;
import ru.party.searchservice.transformer.MeetingEventTransformer;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Controller
@RequestMapping("/api/events/find")
public class SearchMeetingEventController {

    private final MeetingEventRepository meetingEventRepository;
    private final MeetingEventTransformer meetingEventTransformer = new MeetingEventTransformer();

    @Autowired
    public SearchMeetingEventController(MeetingEventRepository meetingEventRepository) {
        this.meetingEventRepository = meetingEventRepository;
    }

    @GetMapping
    public ResponseEntity<List<MeetingEventTO>> findByQuery(
            @RequestParam(name = "q") String query) {
        return ResponseEntity.ok(
                meetingEventRepository.search(queryStringQuery(query)).stream()
                        .map(meetingEventTransformer::transform)
                        .collect(Collectors.toList()));
    }

}