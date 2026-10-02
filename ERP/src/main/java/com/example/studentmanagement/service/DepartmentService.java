package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.DepartmentRequest;
import com.example.studentmanagement.dto.DepartmentResponse;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Department;
import com.example.studentmanagement.exception.DepartmentInUseException;
import com.example.studentmanagement.exception.DepartmentNotFoundException;
import com.example.studentmanagement.exception.DuplicateDepartmentCodeException;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DepartmentService {
	private final DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	private final StudentService studentService;

	public DepartmentService(DepartmentRepository departmentRepository, StudentRepository studentRepository, StudentService studentService) {
		this.departmentRepository = departmentRepository;
		this.studentRepository = studentRepository;
		this.studentService = studentService;
	}

	public List<DepartmentResponse> getAll() {
		return departmentRepository.findAll().stream().map(this::toResponse).toList();
	}

	public DepartmentResponse getById(Long id) {
		return toResponse(findDepartment(id));
	}

	public List<StudentResponse> getStudents(Long id) {
		findDepartment(id);
		return studentService.getByDepartmentId(id);
	}

	@Transactional
	public DepartmentResponse create(DepartmentRequest request) {
		String code = normalizeCode(request.departmentCode());
		if (departmentRepository.existsByDepartmentCode(code)) throw new DuplicateDepartmentCodeException();
		Department department = new Department(code, request.departmentName().trim());
		return toResponse(departmentRepository.save(department));
	}

	@Transactional
	public DepartmentResponse update(Long id, DepartmentRequest request) {
		Department department = findDepartment(id);
		String code = normalizeCode(request.departmentCode());
		if (departmentRepository.existsByDepartmentCodeAndIdNot(code, id)) throw new DuplicateDepartmentCodeException();
		department.setDepartmentCode(code);
		department.setDepartmentName(request.departmentName().trim());
		return toResponse(departmentRepository.save(department));
	}

	@Transactional
	public void delete(Long id) {
		Department department = findDepartment(id);
		if (studentRepository.existsByDepartmentId(id)) throw new DepartmentInUseException();
		departmentRepository.delete(department);
	}

	private Department findDepartment(Long id) {
		return departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
	}

	private DepartmentResponse toResponse(Department department) {
		return new DepartmentResponse(department.getId(), department.getDepartmentCode(), department.getDepartmentName());
	}

	private String normalizeCode(String code) {
		return code.trim().toUpperCase(Locale.ROOT);
	}
}