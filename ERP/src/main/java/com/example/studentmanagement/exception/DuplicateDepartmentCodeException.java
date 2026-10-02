package com.example.studentmanagement.exception;

public class DuplicateDepartmentCodeException extends RuntimeException {
	public DuplicateDepartmentCodeException() { super("Department code already exists"); }
}