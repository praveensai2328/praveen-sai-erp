package com.example.studentmanagement.dto;

import java.time.LocalDateTime;

public record StudentResponse(
		Long id,
		String registerNo,
		String name,
		String email,
		String phone,
		DepartmentSummary department,
		Integer year,
		Integer semester,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}