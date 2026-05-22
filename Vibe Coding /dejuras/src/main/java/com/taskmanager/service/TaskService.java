package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.TaskStatsResponse;
import com.taskmanager.entity.Task;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    List<TaskResponse> getTasksByStatus(Task.Status status);

    List<TaskResponse> getTasksByPriority(Task.Priority priority);

    List<TaskResponse> searchTasks(String keyword);

    TaskResponse updateTask(Long id, TaskRequest request);

    TaskResponse patchStatus(Long id, Task.Status status);

    void deleteTask(Long id);

    TaskStatsResponse getStats();
}
