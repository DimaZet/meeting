package ru.party.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.party.meeting.service.RoleService;

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

	@BeforeAll
	static void before(@Autowired RoleService roleService, @Autowired MockMvc mockMvc) throws Exception {
		roleService.createRole("ROLE_USER");

		ObjectMapper mapper = new ObjectMapper();
		String registerMe = mapper.writeValueAsString(
				new CreateUserRequest("admin", "admin", "admin", "admin"));

		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(registerMe))
				.andExpect(status().isOk());

		String loginMe = mapper.writeValueAsString(
				new LoginUserRequest("admin", "admin"));

		token = mockMvc.perform(get("/tokens")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginMe))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testRegisterManyUsers() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String userOne = mapper.writeValueAsString(
				new CreateUserRequest("one", "admin", "admin", "admin"));
		String userTwo = mapper.writeValueAsString(
				new CreateUserRequest("two", "admin", "admin", "admin"));
		String userThree = mapper.writeValueAsString(
				new CreateUserRequest("three", "admin", "admin", "admin"));

		mockMvc.perform(post("/api/roles")
				.param("name", "USER"))
				.andDo(print())
				.andExpect(status().isForbidden());

		register(userOne)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("username").value("one"))
				.andExpect(jsonPath("firstName").value("admin"))
				.andExpect(jsonPath("lastName").value("admin"))
				.andExpect(jsonPath("roles[0].name").value("ROLE_USER"));
		register(userOne).andExpect(status().isBadRequest()); //Twice registration
		register(userTwo).andExpect(status().isOk());
		register(userThree).andExpect(status().isOk());

		mockMvc.perform(get("/api/roles")
				.header("Authorization", "Bearer " + token))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/users")
				.header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
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
