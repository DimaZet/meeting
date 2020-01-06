package ru.party.meeting.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meeting.dto.MeetingEventTO;
import ru.party.meeting.service.MeetingEventService;
import ru.party.meeting.transformer.MeetingEventTransformer;

@Controller
@RequestMapping("/api/events")
public class MeetingEventController {

    private final MeetingEventService meetingEventService;
    private final MeetingEventTransformer transformer = new MeetingEventTransformer();

    public MeetingEventController(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
    }

    @PostMapping
    public ResponseEntity<MeetingEventTO> createEvent(
            @RequestParam(name = "title") String title, @RequestParam(name = "description") String description) {
        return ResponseEntity.ok(
                transformer.transform(
                        meetingEventService.createEvent(title, description)));
    }

    @GetMapping
    public ResponseEntity<List<MeetingEventTO>> getAllEvents() {
        return ResponseEntity.ok(
                meetingEventService.getAllEvents().stream()
                        .map(transformer::transform)
                        .collect(Collectors.toList()));
    }
}
