package ru.party.searchservice.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import ru.party.searchservice.dto.MeetingEventTO;
import ru.party.searchservice.transformer.MeetingEventTransformer;

@Slf4j
@Service
public class MessageSubscriber implements MessageListener {

    private final MeetingEventTransformer transformer = new MeetingEventTransformer();
    private final MeetingEventService meetingEventService;
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public MessageSubscriber(MeetingEventService meetingEventService) {
        this.meetingEventService = meetingEventService;
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.findAndRegisterModules();
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("Indexing message {} from topic {}", message, new String(bytes));
        try {
            MeetingEventTO event = MAPPER.readValue(message.getBody(), MeetingEventTO.class);
            meetingEventService.index(transformer.transform(event));
        } catch (IOException e) {
            log.error("Message parsing error", e);
        }
    }
}
