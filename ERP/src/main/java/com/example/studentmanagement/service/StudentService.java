package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.DepartmentSummary;
import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DepartmentNotFoundException;
import com.example.studentmanagement.exception.DuplicateRegisterNumberException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {
	private final StudentRepository studentRepository;
	private final DepartmentRepository departmentRepository;

	public StudentService(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
		this.studentRepository = studentRepository;
		this.departmentRepository = departmentRepository;
	}

	public List<StudentResponse> getAll(String department, Integer year, Integer semester) {
		Specification<Student> specification = filterSpecification(department, year, semester);
		return studentRepository.findAll(specification).stream().map(this::toResponse).toList();
	}

	public List<StudentResponse> search(String name, String registerNo) {
		String normalizedName = normalize(name);
		String normalizedRegisterNo = normalize(registerNo);
		Specification<Student> specification = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (normalizedName != null) {
				predicates.add(builder.like(builder.lower(root.get("name")), "%" + normalizedName.toLowerCase(Locale.ROOT) + "%"));
			}
			if (normalizedRegisterNo != null) {
				predicates.add(builder.like(builder.lower(root.get("registerNo")), "%" + normalizedRegisterNo.toLowerCase(Locale.ROOT) + "%"));
			}
			return predicates.isEmpty() ? builder.conjunction() : builder.or(predicates.toArray(Predicate[]::new));
		};
		return studentRepository.findAll(specification).stream().map(this::toResponse).toList();
	}

	public StudentResponse getById(Long id) {
		return toResponse(findStudent(id));
	}

	public List<StudentResponse> getByDepartmentId(Long departmentId) {
		return studentRepository.findAllByDepartmentId(departmentId).stream().map(this::toResponse).toList();
	}

	@Transactional
	public StudentResponse create(StudentRequest request) {
		String registerNo = request.registerNo().trim();
		if (studentRepository.existsByRegisterNo(registerNo)) {
			throw new DuplicateRegisterNumberException();
		}
		Student student = new Student();
		applyRequest(student, request, registerNo);
		return toResponse(studentRepository.save(student));
	}

	@Transactional
	public StudentResponse update(Long id, StudentRequest request) {
		Student student = findStudent(id);
		String registerNo = request.registerNo().trim();
		if (studentRepository.existsByRegisterNoAndIdNot(registerNo, id)) {
			throw new DuplicateRegisterNumberException();
		}
		applyRequest(student, request, registerNo);
		return toResponse(studentRepository.save(student));
	}

	@Transactional
	public void delete(Long id) {
		studentRepository.delete(findStudent(id));
	}

	public Map<String, Object> getStatistics() {
		Map<String, Long> departmentWise = new LinkedHashMap<>();
		studentRepository.countStudentsByDepartment().forEach(row -> departmentWise.put((String) row[0], (Long) row[1]));
		Map<String, Long> yearWise = new LinkedHashMap<>();
		studentRepository.countStudentsByYear().forEach(row -> yearWise.put(String.valueOf(row[0]), (Long) row[1]));
		Map<String, Object> statistics = new LinkedHashMap<>();
		statistics.put("totalStudents", studentRepository.count());
		statistics.put("departmentWise", departmentWise);
		statistics.put("yearWise", yearWise);
		return statistics;
	}

	private Specification<Student> filterSpecification(String department, Integer year, Integer semester) {
		String normalizedDepartment = normalize(department);
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (normalizedDepartment != null) {
				var departmentJoin = root.join("department", JoinType.INNER);
				predicates.add(builder.equal(builder.upper(departmentJoin.get("departmentCode")), normalizedDepartment.toUpperCase(Locale.ROOT)));
			}
			if (year != null) predicates.add(builder.equal(root.get("year"), year));
			if (semester != null) predicates.add(builder.equal(root.get("semester"), semester));
			return builder.and(predicates.toArray(Predicate[]::new));
		};
	}

	private Student findStudent(Long id) {
		return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
	}

	private void applyRequest(Student student, StudentRequest request, String registerNo) {
		var department = departmentRepository.findById(request.departmentId())
				.orElseThrow(() -> new DepartmentNotFoundException(request.departmentId()));
		student.setRegisterNo(registerNo);
		student.setName(request.name().trim());
		student.setEmail(request.email().trim());
		student.setPhone(request.phone().trim());
		student.setDepartment(department);
		student.setYear(request.year());
		student.setSemester(request.semester());
	}

	private StudentResponse toResponse(Student student) {
		var department = student.getDepartment();
		return new StudentResponse(student.getId(), student.getRegisterNo(), student.getName(), student.getEmail(),
				student.getPhone(), new DepartmentSummary(department.getId(), department.getDepartmentCode(), department.getDepartmentName()),
				student.getYear(), student.getSemester(), student.getCreatedAt(), student.getUpdatedAt());
	}

	private String normalize(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}
}