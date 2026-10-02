package com.example.studentmanagement.exception;

public class DuplicateRegisterNumberException extends RuntimeException {
	public DuplicateRegisterNumberException() { super("Register number already exists"); }
}