package ru.party.meetingservice.controller;

import java.util.UUID;
import java.util.stream.Collectors;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meetingservice.dto.CreateEventRequest;
import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.exception.NotFoundException;
import ru.party.meetingservice.exception.UserPrivilegesException;
import ru.party.meetingservice.model.MeetingEvent;
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
    public ResponseEntity createEvent(
            @RequestHeader(name = "username", defaultValue = "") String username,
            @RequestBody CreateEventRequest request) {
        if (StringUtil.isNullOrEmpty(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized");
        }
        MeetingEvent createdEvent = meetingEventService.createEvent(
                username, request.getTitle(), request.getDescription());
        return ResponseEntity.ok(
                transformer.transform(createdEvent));
    }

    @PutMapping
    public ResponseEntity updateEvent(
            @RequestHeader(name = "username", defaultValue = "") String username,
            @RequestParam(name = "id") UUID id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description) {
        if (StringUtil.isNullOrEmpty(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized");
        }
        try {
            return ResponseEntity.ok(
                    transformer.transform(
                            meetingEventService.updateEvent(username, id, title, description)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserPrivilegesException e) {
            return ResponseEntity.badRequest().body("No permissions");
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
    public ResponseEntity deleteEventById(
            @RequestHeader(name = "username", defaultValue = "") String username,
            @PathVariable(name = "id") UUID id) {
        if (StringUtil.isNullOrEmpty(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized");
        }
        try {
            meetingEventService.deleteEventById(username, id);
        } catch (NotFoundException e) {
            log.info("In deleteEventById: event by id not found");
        } catch (UserPrivilegesException e) {
            return ResponseEntity.badRequest().body("No permissions");
        }
        return ResponseEntity.noContent().build();
    }
}
