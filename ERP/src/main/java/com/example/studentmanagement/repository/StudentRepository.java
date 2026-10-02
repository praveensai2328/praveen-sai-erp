package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
	boolean existsByRegisterNo(String registerNo);
	boolean existsByRegisterNoAndIdNot(String registerNo, Long id);
	boolean existsByDepartmentId(Long departmentId);
	List<Student> findAllByDepartmentId(Long departmentId);

	@Query("select s.department.departmentCode, count(s) from Student s group by s.department.departmentCode")
	List<Object[]> countStudentsByDepartment();

	@Query("select s.year, count(s) from Student s group by s.year")
	List<Object[]> countStudentsByYear();
}