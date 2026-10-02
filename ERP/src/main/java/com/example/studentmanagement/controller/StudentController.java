package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/students")
public class StudentController {
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public List<StudentResponse> getAll(
			@RequestParam(required = false) String department,
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer semester) {
		return studentService.getAll(department, year, semester);
	}

	@GetMapping("/search")
	public List<StudentResponse> search(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String registerNo) {
		return studentService.search(name, registerNo);
	}

	@GetMapping("/statistics")
	public Map<String, Object> statistics() {
		return studentService.getStatistics();
	}

	@GetMapping("/{id:\\d+}")
	public StudentResponse getById(@PathVariable Long id) {
		return studentService.getById(id);
	}

	@PostMapping
	public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
		StudentResponse created = studentService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id:\\d+}")
	public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
		return studentService.update(id, request);
	}

	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		studentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}