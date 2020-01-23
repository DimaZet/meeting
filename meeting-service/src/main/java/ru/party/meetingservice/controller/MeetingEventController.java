package ru.party.meetingservice.controller;

import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meetingservice.dto.CreateEventRequest;
import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.exception.NotFoundException;
import ru.party.meetingservice.service.MeetingEventService;
import ru.party.meetingservice.transformer.MeetingEventTransformer;

@Slf4j
@Controller
@RequestMapping("/api/events")
public class MeetingEventController {

    private final MeetingEventService meetingEventService;
    private final MeetingEventTransformer transformer = new MeetingEventTransformer();

    public MeetingEventController(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
    }

    @PostMapping
    public ResponseEntity<MeetingEventTO> createEvent(@RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(
                transformer.transform(
                        meetingEventService.createEvent(request.getTitle(), request.getDescription())));
    }

    @PutMapping
    public ResponseEntity<MeetingEventTO> updateEvent(
            @RequestParam(name = "id") UUID id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description) {
        try {
            return ResponseEntity.ok(
                    transformer.transform(
                            meetingEventService.updateEvent(id, title, description)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingEventTO> findEventById(@PathVariable(name = "id") UUID id) {
        try {
            return ResponseEntity.ok(
                    transformer.transform(
                            meetingEventService.findEventById(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity findAllEvents() {
        return ResponseEntity.ok(
                meetingEventService.findAllEvents().stream()
                        .map(transformer::transform)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEventById(@PathVariable(name = "id") UUID id) {
        try {
            meetingEventService.deleteEventById(id);
        } catch (NotFoundException e) {
            log.info("In deleteEventById: event by id not found");
        }
        return ResponseEntity.noContent().build();
    }
}
