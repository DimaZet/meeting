package ru.party.meeting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
    void testPostEvent() throws Exception {
		String title = "let's go cinema";
		String description = "tomorrow i'd like to go cinema with girl";
		this.mockMvc.perform(post("/events?title={title}&description={description}", title, description))
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(title))
				.andExpect(jsonPath("description").value(description))
				.andExpect(jsonPath("createdAt").isNotEmpty());
	}

	@Test
    void testGetEvents() throws Exception {
		this.mockMvc.perform(get("/events"))
				.andDo(print())
				.andExpect(status().is(200));
	}

}
