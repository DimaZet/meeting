package ru.party.meetingservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.model.MeetingEvent;
import ru.party.meetingservice.transformer.MeetingEventTransformer;

@Slf4j
@Component
public class MeetingEventListener extends AbstractMongoEventListener<MeetingEvent> {

    private static final ObjectMapper MAPPER = Jackson2ObjectMapperBuilder
            .json()
            .modulesToInstall(new JavaTimeModule())
            .modulesToInstall(new ParameterNamesModule())
            .build();
    private final RestTemplate client = new RestTemplate();
    private final String baseUrl;
    private final String index;
    private final MeetingEventTransformer transformer = new MeetingEventTransformer();

    @Autowired
    public MeetingEventListener(
            @Value("${searchservice.api.url}") String baseUrl,
            @Value("${searchservice.api.index}") String index) {
        this.baseUrl = baseUrl;
        this.index = index;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<MeetingEvent> event) {
        try {
            log.info("{} {} {}", baseUrl + index, HttpMethod.PUT, mapToJson(event.getSource()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            MeetingEventTO indexed = client.exchange(
                    baseUrl + index,
                    HttpMethod.PUT,
                    new HttpEntity<>(event.getSource(), headers),
                    MeetingEventTO.class
            ).getBody();
            log.info("Event with id {} successfully indexed", indexed.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            log.error("Request failed. Cannot connect to search-service");
            throw new RuntimeException(e);
        }
        super.onAfterSave(event);
    }

    private String mapToJson(MeetingEvent event) throws JsonProcessingException {
        return MAPPER.writeValueAsString(
                transformer.transform(event));
    }
}
