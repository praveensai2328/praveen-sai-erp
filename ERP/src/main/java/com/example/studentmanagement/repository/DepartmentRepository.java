package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByDepartmentCode(String departmentCode);
	boolean existsByDepartmentCodeAndIdNot(String departmentCode, Long id);
}