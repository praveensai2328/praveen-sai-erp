package com.example.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
		@NotBlank @Size(max = 30) String departmentCode,
		@NotBlank @Size(max = 150) String departmentName) {
}