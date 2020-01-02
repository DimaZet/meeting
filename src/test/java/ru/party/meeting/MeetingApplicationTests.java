package ru.party.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.party.meeting.dto.CreateUserRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class}) //TODO test with security
public class MeetingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testRegisterManyUsers() throws Exception {
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
				.andExpect(status().isOk());

		register(userOne)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("login").value("one"))
				.andExpect(jsonPath("firstName").value("admin"))
				.andExpect(jsonPath("lastName").value("admin"))
				.andExpect(jsonPath("roles[0].name").value("USER"));
		register(userOne).andExpect(status().isBadRequest()); //Twice registration
		register(userTwo).andExpect(status().isOk());
		register(userThree).andExpect(status().isOk());

		mockMvc.perform(get("/api/roles"))
				.andDo(print())
				.andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
	}

	@Test
	public void testPostEvent() throws Exception {
		String title = "let's go cinema";
		String description = "tomorrow i'd like to go cinema with girl";
		mockMvc.perform(post("/api/events?title={title}&description={description}", title, description))
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(title))
				.andExpect(jsonPath("description").value(description))
				.andExpect(jsonPath("createdAt").isNotEmpty());
	}

	@Test
	public void testGetEvents() throws Exception {
		mockMvc.perform(get("/api/events"))
				.andDo(print())
				.andExpect(status().is(200));
	}

	private ResultActions register(String userJson) throws Exception {
        return mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson))
				.andDo(print());
	}
}
