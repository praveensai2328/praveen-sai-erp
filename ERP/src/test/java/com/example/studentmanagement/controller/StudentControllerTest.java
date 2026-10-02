package com.example.studentmanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.studentmanagement.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class StudentControllerTest {
	private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(null))
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();

	@Test
	void rejectsInvalidStudentWithStructuredValidationErrors() throws Exception {
		String request = """
				{"registerNo":"","name":"Arun","email":"arun@","phone":"12345","departmentId":1,"year":5,"semester":9}
				""";

		mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("Validation failed"))
			.andExpect(jsonPath("$.errors.registerNo").exists())
			.andExpect(jsonPath("$.errors.email").value("Invalid email format"))
			.andExpect(jsonPath("$.errors.phone").exists())
			.andExpect(jsonPath("$.errors.year").exists())
			.andExpect(jsonPath("$.errors.semester").exists());
	}
}