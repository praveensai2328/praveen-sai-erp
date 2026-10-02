# Student Management Backend

A Spring Boot REST API for managing students and departments. It uses DTOs for the HTTP contract, Bean Validation for request validation, and Spring Data JPA to persist data in MySQL.

## Technologies

Java 17, Spring Boot 3, Spring Web, Spring Data JPA, MySQL, Maven, Bean Validation, Postman, and Git/GitHub.

## Requirements

- Java 17 or newer
- MySQL Server 8 or compatible
- Maven (or use the included Maven wrapper)
- Postman for API requests
- Git for version control

## Database Setup

Start MySQL, then create the database and tables using the provided script:

```sql
CREATE DATABASE student_management;
```

Or run the full script from the project root:

```powershell
mysql -u root -p < database/student_management.sql
```

The application uses `jdbc:mysql://localhost:3306/student_management`. Hibernate updates the schema on startup, and the SQL script can also be used to create the tables independently.

### Credentials

No real password is committed. The tracked `src/main/resources/application.properties` contains `YOUR_MYSQL_PASSWORD` as a safe default. For local use, create an ignored `application-local.properties` in the project root by copying `src/main/resources/application-example.properties`, then set your own MySQL username and password there. The main configuration imports this optional local override. Alternatively, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables. Do not commit credentials.

The server listens on port `2328`.

## Run and Test

```powershell
./mvnw.cmd clean test
./mvnw.cmd spring-boot:run
```

Use `mvn` instead of `./mvnw.cmd` if Maven is installed globally. The API base URL is `http://localhost:2328`. Startup requires MySQL to be running, the database to exist, and valid local credentials. On startup, five default departments are inserted only when their codes are missing.

## API Documentation

All request and response bodies use JSON. Successful student/department creation returns `201 Created` and a `Location` header; successful reads and updates return `200 OK`; deletes return `204 No Content`. Invalid request bodies return `400`, missing records return `404`, duplicate codes/register numbers and attempts to delete an in-use department return `409`.

### Departments

| Method | URL | Purpose | Body | Success |
| --- | --- | --- | --- | --- |
| GET | `/api/departments` | List departments | None | 200, array of departments |
| GET | `/api/departments/{id}` | Get a department | None | 200, department |
| POST | `/api/departments` | Create a department | `{"departmentCode":"IT","departmentName":"Information Technology"}` | 201, department |
| PUT | `/api/departments/{id}` | Update a department | Same shape as POST | 200, department |
| DELETE | `/api/departments/{id}` | Delete an unused department | None | 204 |
| GET | `/api/departments/{id}/students` | List students in a department | None | 200, array of students |

Department codes are unique. A department with students cannot be deleted.

### Students

| Method | URL | Purpose | Body | Success |
| --- | --- | --- | --- | --- |
| GET | `/api/students` | List students; optional filters below | None | 200, array of students |
| GET | `/api/students/{id}` | Get a student | None | 200, student |
| POST | `/api/students` | Create a student | Student JSON below | 201, student |
| PUT | `/api/students/{id}` | Replace a student's fields | Student JSON below | 200, student |
| DELETE | `/api/students/{id}` | Delete a student | None | 204 |
| GET | `/api/students/search?name=Arun` | Case-insensitive partial name search | None | 200, array of students |
| GET | `/api/students/search?registerNo=23IT` | Partial register-number search | None | 200, array of students |
| GET | `/api/students/statistics` | Student totals by department and year | None | 200, statistics object |

Student request body:

```json
{
  "registerNo": "23IT001",
  "name": "Arun Kumar",
  "email": "arun@example.com",
  "phone": "9876543210",
  "departmentId": 1,
  "year": 3,
  "semester": 5
}
```

`registerNo` must be unique, `departmentId` must exist, `year` must be 1-4, `semester` 1-8, and `phone` must be a valid 10-digit Indian mobile number. Filters can be combined: `GET /api/students?department=IT&year=3&semester=5`. Filter parameters are `department` (department code), `year`, and `semester`.

Student responses include `id`, the submitted student fields, a compact department object (`id`, `departmentCode`, `departmentName`), `createdAt`, and `updatedAt`. Statistics are aggregated by the database rather than loading all student entities.

## Errors

Errors are JSON. For example, duplicate register numbers return `409` with `{"status":409,"message":"Register number already exists"}`. Validation failures return `400` with `status`, `message: "Validation failed"`, and an `errors` map keyed by field. Unexpected errors return a generic `500` response without stack traces or sensitive details.

## MySQL Connectivity

Spring Boot reads its JDBC URL and credentials from `application.properties` or the local override/environment variables. Spring Data JPA and Hibernate use that connection to create/update `departments` and `students`; REST controllers call services, which persist through repositories. If startup reports a connection error, verify MySQL is running, `student_management` exists, and the configured user/password are correct.

## Postman

Import `postman/Student-Management-API.postman_collection.json` into Postman. The collection defines `baseUrl` as `http://localhost:2328`; the separate `postman/Student-Management-API.postman_environment.json` can also be imported and selected. The collection includes CRUD, search, filters, statistics, validation failures, duplicates, and not-found examples.

## GitHub

```bash
git init
git add .
git commit -m "Initial student management backend"
git branch -M main
git remote add origin <YOUR_GITHUB_REPOSITORY_URL>
git push -u origin main
```