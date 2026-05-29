package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.TaskStatsResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    // ─── POST /api/v1/tasks ──────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    // ─── GET /api/v1/tasks ───────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns all tasks. Optionally filter by status or priority.")
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @Parameter(description = "Filter by status")
            @RequestParam(required = false) Task.Status status,
            @Parameter(description = "Filter by priority")
            @RequestParam(required = false) Task.Priority priority
    ) {
        if (status != null && priority != null) {
            return ResponseEntity.ok(
                    taskService.getAllTasks().stream()
                            .filter(t -> t.getStatus() == status && t.getPriority() == priority)
                            .toList()
            );
        }
        if (status != null) return ResponseEntity.ok(taskService.getTasksByStatus(status));
        if (priority != null) return ResponseEntity.ok(taskService.getTasksByPriority(priority));
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // ─── GET /api/v1/tasks/search ────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Search tasks by keyword in title or description")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        return ResponseEntity.ok(taskService.searchTasks(keyword));
    }

    // ─── GET /api/v1/tasks/stats ─────────────────────────────────────────────

    @GetMapping("/stats")
    @Operation(summary = "Get task statistics")
    public ResponseEntity<TaskStatsResponse> getStats() {
        return ResponseEntity.ok(taskService.getStats());
    }

    // ─── GET /api/v1/tasks/{id} ──────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    public ResponseEntity<TaskResponse> getTaskById(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // ─── PUT /api/v1/tasks/{id} ──────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Fully update a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    // ─── PATCH /api/v1/tasks/{id}/status ────────────────────────────────────

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update only the status of a task")
    public ResponseEntity<TaskResponse> patchStatus(
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam Task.Status status) {
        return ResponseEntity.ok(taskService.patchStatus(id, status));
    }

    // ─── DELETE /api/v1/tasks/{id} ───────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}