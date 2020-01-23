package ru.party.searchservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.searchservice.dto.MeetingEventTO;
import ru.party.searchservice.model.MeetingEvent;
import ru.party.searchservice.service.MeetingEventService;
import ru.party.searchservice.transformer.MeetingEventTransformer;

@Controller
@RequestMapping("/api/events")
public class MeetingEventController {

    private final MeetingEventService meetingEventService;
    private final MeetingEventTransformer transformer = new MeetingEventTransformer();

    @Autowired
    public MeetingEventController(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
    }

    @PutMapping("/index")
    public ResponseEntity<MeetingEventTO> index(@RequestBody MeetingEventTO meetingEventTO) {
        MeetingEvent indexed = meetingEventService.index(
                transformer.transform(meetingEventTO));
        return ResponseEntity.ok(
                transformer.transform(indexed));
    }

    @GetMapping("/find")
    public ResponseEntity<List<MeetingEventTO>> findByQuery(
            @RequestParam(name = "q") String stringQuery) {
        return ResponseEntity.ok(
                meetingEventService.searchByWord(stringQuery).stream()
                        .map(transformer::transform)
                        .collect(Collectors.toList()));
    }

}