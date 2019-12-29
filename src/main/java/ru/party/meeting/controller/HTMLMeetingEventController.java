package ru.party.meeting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meeting.model.MeetingEvent;
import ru.party.meeting.service.MeetingEventService;

import java.util.List;

@Controller
public class HTMLMeetingEventController {

    private final MeetingEventService meetingEventService;

    public HTMLMeetingEventController(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {

        model.addAttribute("eventsList", meetingEventService.getAllEvents());
        model.addAttribute("added", false);
        return "events-list";
    }

    @PostMapping("/events")
    public String createEvent(
            @RequestParam(name = "title") String title, @RequestParam(name = "description") String description, Model model) {
        meetingEventService.createEvent(title, description);
        model.addAttribute("eventsList", meetingEventService.getAllEvents());
        model.addAttribute("added", true);
        return "events-list";
    }

}
