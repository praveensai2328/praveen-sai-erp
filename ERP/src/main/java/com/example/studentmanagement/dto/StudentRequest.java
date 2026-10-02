package com.example.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudentRequest(
		@NotBlank @Size(max = 50) String registerNo,
		@NotBlank @Size(max = 120) String name,
		@NotBlank @Email(message = "Invalid email format") @Size(max = 254) String email,
		@NotBlank @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be a valid 10-digit Indian mobile number") String phone,
		@NotNull Long departmentId,
		@NotNull @Min(1) @Max(4) Integer year,
		@NotNull @Min(1) @Max(8) Integer semester) {
}