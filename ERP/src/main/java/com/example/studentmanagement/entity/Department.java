package com.example.studentmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "department_code", nullable = false, unique = true, length = 30)
	private String departmentCode;

	@Column(name = "department_name", nullable = false, length = 150)
	private String departmentName;

	@OneToMany(mappedBy = "department")
	private List<Student> students = new ArrayList<>();

	protected Department() {
	}

	public Department(String departmentCode, String departmentName) {
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
	}

	public Long getId() { return id; }
	public String getDepartmentCode() { return departmentCode; }
	public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public List<Student> getStudents() { return students; }
}