package ru.party.meetingservice;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.party.meetingservice.dto.CreateEventRequest;
import ru.party.meetingservice.dto.MeetingEventTO;
import ru.party.meetingservice.service.RedisMessagePublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MeetingServiceApplicationTest {

    private final static ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @SpyBean
    RedisMessagePublisher messagePublisher;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        doNothing().when(messagePublisher).publish(any());
    }

    @Test
    public void successfulPostAndUpdate() throws Exception {
        String createEventJsonReq = MAPPER.writeValueAsString(new CreateEventRequest("first", "successful"));
        String createdEventJsonResp = mockMvc.perform(post("/api/events")
                .header("username", "dima")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createEventJsonReq))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO createdEvent = MAPPER.readValue(createdEventJsonResp, MeetingEventTO.class);

        assertThat(createdEvent).satisfies(event -> {
            assertThat(event.getCreatedBy()).isEqualTo("dima");
            assertThat(event.getLastModifiedBy()).isEqualTo("dima");
            assertThat(event).hasNoNullFieldsOrProperties();
        });

        UUID id = createdEvent.getId();
        String updatedEventJsonResp = mockMvc.perform(put("/api/events?id={id}&title={title}&description={description}",
                id, "first_updated", "successful_updated")
                .header("username", "dima"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO updatedEvent = MAPPER.readValue(updatedEventJsonResp, MeetingEventTO.class);

        assertThat(updatedEvent).satisfies(event -> {
            assertThat(event.getLastModifiedBy()).isEqualTo("dima");
            assertThat(event.getUpdatedAt()).isAfter(createdEvent.getUpdatedAt());
            assertThat(event.getTitle()).isEqualTo("first_updated");
            assertThat(event.getDescription()).isEqualTo("successful_updated");
            assertThat(event).hasNoNullFieldsOrProperties();
        });

        mockMvc.perform(put("/api/events?id={id}&title={title}&description={description}",
                id, "first_updated2", "successful_updated2")
                .header("username", "evgenys"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/events?id={id}&title={title}&description={description}",
                id, "first_updated2", "successful_updated2"))
                .andExpect(status().isForbidden());
    }
}