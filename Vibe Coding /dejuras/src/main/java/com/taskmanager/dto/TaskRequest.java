package com.taskmanager.dto;

import com.taskmanager.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload to create or update a task")
public class TaskRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    @Schema(description = "Task title", example = "Fix login bug", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Detailed description of the task", example = "Users cannot log in with SSO credentials")
    private String description;

    @NotNull(message = "Priority is required")
    @Schema(description = "Task priority level", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    private Task.Priority priority;

    @NotNull(message = "Status is required")
    @Schema(description = "Current task status", example = "TODO", requiredMode = Schema.RequiredMode.REQUIRED)
    private Task.Status status;
}
