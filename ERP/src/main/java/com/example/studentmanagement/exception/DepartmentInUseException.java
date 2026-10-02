package com.example.studentmanagement.exception;

public class DepartmentInUseException extends RuntimeException {
	public DepartmentInUseException() { super("Department cannot be deleted while students belong to it"); }
}