package com.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task statistics summary")
public class TaskStatsResponse {

    @Schema(description = "Total number of tasks")
    private long total;

    @Schema(description = "Tasks with TODO status")
    private long todo;

    @Schema(description = "Tasks in progress")
    private long inProgress;

    @Schema(description = "Completed tasks")
    private long done;

    @Schema(description = "Cancelled tasks")
    private long cancelled;
}
