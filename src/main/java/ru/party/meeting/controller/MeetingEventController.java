package ru.party.meeting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.service.MeetingEventService;

@Controller
@RequestMapping(path = "/api")
public class MeetingEventController {

    private final MeetingEventService meetingEventService;

    public MeetingEventController(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
    }

    @PostMapping("/events")
    public ResponseEntity<MeetingEvent> createEvent(
            @RequestParam(name = "title") String title, @RequestParam(name = "description") String description) {
        return ResponseEntity.ok(
                meetingEventService.createEvent(title, description));
    }

    @GetMapping("/events")
    public ResponseEntity<List<MeetingEvent>> getAllEvents() {
        return ResponseEntity.ok(
                meetingEventService.getAllEvents());
    }
}
