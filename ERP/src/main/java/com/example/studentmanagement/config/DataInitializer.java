package com.example.studentmanagement.config;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.studentmanagement.entity.Department;
import com.example.studentmanagement.repository.DepartmentRepository;

@Component
public class DataInitializer implements ApplicationRunner {
	private final DepartmentRepository departmentRepository;

	public DataInitializer(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		List<Department> defaults = List.of(
				new Department("CSE", "Computer Science and Engineering"),
				new Department("IT", "Information Technology"),
				new Department("ECE", "Electronics and Communication Engineering"),
				new Department("MECH", "Mechanical Engineering"),
				new Department("AIML", "Artificial Intelligence and Machine Learning"));
		for (Department department : defaults) {
			if (!departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {
				departmentRepository.save(department);
			}
		}
	}
}