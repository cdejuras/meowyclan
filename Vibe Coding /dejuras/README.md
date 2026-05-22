# TaskFlow — Task Manager API

A production-ready full stack Spring Boot application for managing tasks.

---

## 🧰 Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Framework    | Spring Boot 3.2.5                 |
| Build Tool   | Gradle 8.7                        |
| Persistence  | Spring Data JPA + H2 (in-memory)  |
| API Docs     | SpringDoc OpenAPI (Swagger UI)    |
| Boilerplate  | Lombok                            |
| Dev Tool     | Spring Boot DevTools              |
| Testing      | JUnit 5 + Mockito                 |
| Frontend     | Vanilla HTML/CSS/JS (SPA)         |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Gradle (or use the included wrapper)

### Run the Application

```bash
./gradlew bootRun
```

The app starts on **http://localhost:8080**

---

## 🔗 Important URLs

| URL                                    | Description         |
|----------------------------------------|---------------------|
| http://localhost:8080                  | Frontend SPA        |
| http://localhost:8080/swagger-ui.html  | Swagger UI          |
| http://localhost:8080/api-docs         | OpenAPI JSON        |
| http://localhost:8080/h2-console       | H2 DB Console       |

**H2 Console credentials:**
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: *(leave blank)*

---

## 📦 API Endpoints

| Method   | Endpoint                       | Description                  |
|----------|--------------------------------|------------------------------|
| `POST`   | `/api/v1/tasks`                | Create a new task            |
| `GET`    | `/api/v1/tasks`                | Get all tasks (with filters) |
| `GET`    | `/api/v1/tasks/{id}`           | Get task by ID               |
| `GET`    | `/api/v1/tasks/search?keyword` | Search by keyword            |
| `GET`    | `/api/v1/tasks/stats`          | Get task statistics          |
| `PUT`    | `/api/v1/tasks/{id}`           | Full update of a task        |
| `PATCH`  | `/api/v1/tasks/{id}/status`    | Update status only           |
| `DELETE` | `/api/v1/tasks/{id}`           | Delete a task                |

### Filter Tasks by Query Params
```
GET /api/v1/tasks?status=TODO
GET /api/v1/tasks?priority=HIGH
GET /api/v1/tasks?status=IN_PROGRESS&priority=CRITICAL
```

---

## 📋 Example Request

```json
POST /api/v1/tasks
{
  "title": "Implement OAuth2",
  "description": "Add Google SSO to the login flow",
  "priority": "HIGH",
  "status": "TODO"
}
```

---

## 🏗️ Project Structure

```
src/main/java/com/taskmanager/
├── TaskManagerApplication.java     # Entry point
├── config/
│   ├── DataSeeder.java             # Sample data on startup
│   └── OpenApiConfig.java          # Swagger customization
├── controller/
│   └── TaskController.java         # REST endpoints
├── dto/
│   ├── TaskRequest.java            # Input DTO (validated)
│   ├── TaskResponse.java           # Output DTO
│   └── TaskStatsResponse.java      # Stats DTO
├── entity/
│   └── Task.java                   # JPA entity
├── exception/
│   ├── TaskNotFoundException.java  # Domain exception
│   └── GlobalExceptionHandler.java # @RestControllerAdvice
├── repository/
│   └── TaskRepository.java         # Spring Data JPA
└── service/
    ├── TaskService.java            # Interface
    ├── TaskServiceImpl.java        # Business logic
    └── TaskMapper.java             # Entity ↔ DTO mapping
```

---

## ✅ Running Tests

```bash
./gradlew test
```

---

## 🔄 Enums

**Priority:** `LOW` | `MEDIUM` | `HIGH` | `CRITICAL`

**Status:** `TODO` | `IN_PROGRESS` | `DONE` | `CANCELLED`
