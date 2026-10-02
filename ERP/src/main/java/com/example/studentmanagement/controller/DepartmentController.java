package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.DepartmentRequest;
import com.example.studentmanagement.dto.DepartmentResponse;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.service.DepartmentService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	public List<DepartmentResponse> getAll() {
		return departmentService.getAll();
	}

	@GetMapping("/{id:\\d+}")
	public DepartmentResponse getById(@PathVariable Long id) {
		return departmentService.getById(id);
	}

	@GetMapping("/{id:\\d+}/students")
	public List<StudentResponse> getStudents(@PathVariable Long id) {
		return departmentService.getStudents(id);
	}

	@PostMapping
	public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
		DepartmentResponse created = departmentService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id:\\d+}")
	public DepartmentResponse update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
		return departmentService.update(id, request);
	}

	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		departmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}