package ru.party.meetingservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.model.MeetingEvent;

@Slf4j
@Component
public class MeetingEventListener extends AbstractMongoEventListener<MeetingEvent> {

    private final RestTemplate client = new RestTemplate();
    private final String baseUrl;
    private final String index;

    @Autowired
    public MeetingEventListener(
            @Value("${searchservice.api.url}") String baseUrl,
            @Value("${searchservice.api.index}") String index) {
        this.baseUrl = baseUrl;
        this.index = index;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<MeetingEvent> event) {
        MeetingEvent savedEvent = event.getSource();
        log.debug("{} {} {}", baseUrl + index, HttpMethod.PUT, savedEvent);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MeetingEventTO indexed = client.exchange(
                baseUrl + index,
                HttpMethod.PUT,
                new HttpEntity<>(savedEvent, headers),
                MeetingEventTO.class
        ).getBody();
        log.info("Event with id {} successfully indexed", indexed.getId());
        super.onAfterSave(event);
    }
}
