package ru.party.meeting;

import java.util.List;

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
import ru.party.meeting.dto.CreateUserRequest;
import ru.party.meeting.dto.LoginUserRequest;
import ru.party.meeting.dto.UserTO;
import ru.party.meeting.service.RoleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
	void testPostEvent() throws Exception {
		String title = "let's go cinema";
		String description = "tomorrow i'd like to go cinema with girl";
		mockMvc.perform(post("/api/events?title={title}&description={description}", title, description)
				.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(title))
				.andExpect(jsonPath("description").value(description))
				.andExpect(jsonPath("createdAt").isNotEmpty());
	}

	@Test
	void testGetEvents() throws Exception {
		mockMvc.perform(get("/api/events")
				.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().is(200));
	}

	private ResultActions register(String userJson) throws Exception {
		return mockMvc.perform(post("/api/users/register")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson))
				.andDo(print());
	}
}
