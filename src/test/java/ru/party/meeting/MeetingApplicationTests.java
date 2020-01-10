package ru.party.meeting;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.party.meeting.dto.CreateEventRequest;
import ru.party.meeting.dto.CreateUserRequest;
import ru.party.meeting.dto.LoginUserRequest;
import ru.party.meeting.dto.MeetingEventTO;
import ru.party.meeting.dto.StatusTO;
import ru.party.meeting.dto.UserTO;
import ru.party.meeting.service.RoleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class MeetingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static String token;

    private final static ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	@BeforeAll
	static void before(@Autowired RoleService roleService, @Autowired MockMvc mockMvc) throws Exception {
        assertThat(roleService.createRole("ROLE_USER"))
                .isNotNull();

		String registerMe = mapper.writeValueAsString(
				new CreateUserRequest("admin", "admin", "admin", "admin"));
        String registeredUserJson = mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(registerMe))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserTO registeredUser = mapper.readValue(registeredUserJson, UserTO.class);
        assertThat(registeredUser).hasNoNullFieldsOrProperties();

        String loginRequestJson = mapper.writeValueAsString(
				new LoginUserRequest("admin", "admin"));
		token = mockMvc.perform(get("/tokens")
				.contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
        assertThat(token).isNotNull();
	}

	@Test
	void testRegisterManyUsers() throws Exception {
		String userOne = mapper.writeValueAsString(
				new CreateUserRequest("one", "admin", "admin", "admin"));
		String userTwo = mapper.writeValueAsString(
				new CreateUserRequest("two", "admin", "admin", "admin"));
		String userThree = mapper.writeValueAsString(
				new CreateUserRequest("three", "admin", "admin", "admin"));

        String registeredUserOneJson = register(userOne)
				.andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserTO registeredUserOne = mapper.readValue(registeredUserOneJson, UserTO.class);
        assertThat(registeredUserOne).satisfies(u -> {
            assertThat(u.getId()).isNotNull();
            assertThat(u.getUsername()).isEqualTo("one");
            assertThat(u.getFirstName()).isEqualTo("admin");
            assertThat(u.getLastName()).isEqualTo("admin");
            assertThat(u.getRoles()).satisfies(roleTOS -> {
                assertThat(roleTOS).hasSize(1);
                assertThat(roleTOS.get(0).getName()).isEqualTo("ROLE_USER");
            });
        });
		register(userTwo).andExpect(status().isOk());
		register(userThree).andExpect(status().isOk());

		mockMvc.perform(get("/api/roles")
				.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk());

        String usersJson = mockMvc.perform(get("/api/users")
				.header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        List<UserTO> users = mapper.readValue(usersJson,
                mapper.getTypeFactory().constructCollectionType(List.class, UserTO.class));

        assertThat(users)
                .hasSize(4)
                .allSatisfy(userTO -> assertThat(userTO).hasNoNullFieldsOrProperties());
	}

	@Test
    void testPostEvent_successfully() throws Exception {
		String title = "let's go cinema";
		String description = "tomorrow i'd like to go cinema with girl";
        String createEventJson = mapper.writeValueAsString(
                new CreateEventRequest(title, description));
        String postedEventJson = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createEventJson)
				.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO postedEvent = mapper.readValue(postedEventJson, MeetingEventTO.class);
        assertThat(postedEvent).satisfies(e -> {
            assertThat(e.getTitle()).isEqualTo(title);
            assertThat(e.getDescription()).isEqualTo(description);
            assertThat(e.getTitle()).isEqualTo(title);
            assertThat(e).hasNoNullFieldsOrProperties();
        });

        String postedEventsJson = mockMvc.perform(get("/api/events")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        List<MeetingEventTO> postedEvents = mapper.readValue(postedEventsJson,
                mapper.getTypeFactory().constructCollectionType(List.class, MeetingEventTO.class));
        assertThat(postedEvents).allSatisfy(
                meetingEventTO -> assertThat(meetingEventTO).hasNoNullFieldsOrProperties()
        );
	}

    @Test
    void testCRUDEvent_successfully() throws Exception {
        //CREATE
        String title = "let's go cinema";
        String description = "tomorrow i'd like to go cinema with girl";
        String postedEventJson = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new CreateEventRequest(title, description)))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO postedEvent = mapper.readValue(postedEventJson, MeetingEventTO.class);
        assertThat(postedEvent).satisfies(event -> {
            assertThat(event.getTitle()).isEqualTo(title);
            assertThat(event.getDescription()).isEqualTo(description);
            assertThat(event).hasNoNullFieldsOrProperties();
        });
        UUID eventId = postedEvent.getId();
        Instant createdAt = postedEvent.getCreatedAt();
        Instant updatedAt = postedEvent.getUpdatedAt();
        //READ
        String gotByIdEventJson = mockMvc.perform(get("/api/events/{id}", eventId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO gotByIdEvent = mapper.readValue(gotByIdEventJson, MeetingEventTO.class);
        assertThat(gotByIdEvent).isEqualToIgnoringGivenFields(postedEvent,
                "createdAt", "updatedAt"); //TODO
        //UPDATE
        String updatedTitle = "let's go cinema";
        String updatedDescription = "tomorrow i'd like to go cinema with girl";
        String updatedEventJson = mockMvc.perform(put("/api/events?" +
                "id={id}&title={title}&description={description}", eventId, updatedTitle, updatedDescription)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO updatedEvent = mapper.readValue(updatedEventJson, MeetingEventTO.class);
        assertThat(updatedEvent).satisfies(event -> {
            assertThat(event.getId()).isEqualTo(eventId);
            assertThat(event.getTitle()).isEqualTo(updatedTitle);
            assertThat(event.getDescription()).isEqualTo(updatedDescription);
            //assertThat(event.getCreatedAt()).isEqualTo(createdAt); //TODO
            //assertThat(event.getUpdatedAt()).isAfter(updatedAt); //TODO
            assertThat(event).hasNoNullFieldsOrProperties();
        });
        //DELETE
        String deletedEventJson = mockMvc.perform(delete("/api/events/{id}", eventId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();
        assertThat(deletedEventJson).isEmpty();
        //FIND DELETED
        String foundDeletedEventJson = mockMvc.perform(get("/api/events/{id}", eventId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MeetingEventTO foundDeletedEvent = mapper.readValue(foundDeletedEventJson, MeetingEventTO.class);
        assertThat(foundDeletedEvent).satisfies(event -> {
            assertThat(event.getStatus()).isEqualTo(StatusTO.DELETED);
            assertThat(event).hasNoNullFieldsOrProperties();
        });
    }

    @Test
    void testUnauthorizedPostEvent() throws Exception {
        String title = "let's go cinema";
        String description = "tomorrow i'd like to go cinema with girl";
        mockMvc.perform(post("/api/events?title={title}&description={description}",
                title, description))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testNotFoundedEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        mockMvc.perform(get("/api/events/{eventId}", eventId)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testWrongToken() throws Exception {
        mockMvc.perform(get("/api/events")
                .header("Authorization", "Bearer " + "wrong"))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/events"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

	private ResultActions register(String userJson) throws Exception {
		return mockMvc.perform(post("/api/users/register")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson))
				.andDo(print());
	}
}
