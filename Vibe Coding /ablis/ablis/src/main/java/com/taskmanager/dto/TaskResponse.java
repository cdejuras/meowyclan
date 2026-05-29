package com.taskmanager.dto;

import com.taskmanager.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task response payload")
public class TaskResponse {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Task title", example = "Fix login bug")
    private String title;

    @Schema(description = "Task description")
    private String description;

    @Schema(description = "Priority level", example = "HIGH")
    private Task.Priority priority;

    @Schema(description = "Current status", example = "TODO")
    private Task.Status status;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp")
    private LocalDateTime updatedAt;
}