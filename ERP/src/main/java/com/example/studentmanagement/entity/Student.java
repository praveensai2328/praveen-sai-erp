package com.example.studentmanagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "register_no", nullable = false, unique = true, length = 50)
	private String registerNo;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(nullable = false, length = 254)
	private String email;

	@Column(nullable = false, length = 20)
	private String phone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	@Column(name = "\"year\"", nullable = false)
	private Integer year;

	@Column(nullable = false)
	private Integer semester;

	@Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt;

	public Student() {
	}

	public Long getId() { return id; }
	public String getRegisterNo() { return registerNo; }
	public void setRegisterNo(String registerNo) { this.registerNo = registerNo; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public Department getDepartment() { return department; }
	public void setDepartment(Department department) { this.department = department; }
	public Integer getYear() { return year; }
	public void setYear(Integer year) { this.year = year; }
	public Integer getSemester() { return semester; }
	public void setSemester(Integer semester) { this.semester = semester; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}