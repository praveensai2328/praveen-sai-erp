package com.example.studentmanagement.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleStudentNotFound(StudentNotFoundException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(DepartmentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFoundException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler({DuplicateRegisterNumberException.class, DuplicateDepartmentCodeException.class, DepartmentInUseException.class})
	public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {
		return response(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error ->
			errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(new ErrorResponse(400, "Validation failed", errors));
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorResponse> handleMalformedRequest(Exception exception) {
		exception.printStackTrace();
		return response(HttpStatus.BAD_REQUEST, "Request contains an invalid value");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataConflict(DataIntegrityViolationException exception) {
		return response(HttpStatus.CONFLICT, "The operation conflicts with existing data");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
		exception.printStackTrace();
		return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	}

	private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
	}
}