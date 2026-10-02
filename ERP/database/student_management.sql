CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    department_code VARCHAR(30) NOT NULL,
    department_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_departments_department_code UNIQUE (department_code)
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    register_no VARCHAR(50) NOT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    department_id BIGINT NOT NULL,
    `year` INT NOT NULL,
    semester INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_students_register_no UNIQUE (register_no),
    CONSTRAINT fk_students_department FOREIGN KEY (department_id) REFERENCES departments (id)
);