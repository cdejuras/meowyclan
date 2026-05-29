package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.dto.TaskStatsResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // Create a Task with title and ID

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        log.info("Creating task with title: {}", request.getTitle());
        Task task = taskMapper.toEntity(request);
        Task saved = taskRepository.save(task);
        log.info("Task created with id: {}", saved.getId());
        return taskMapper.toResponse(saved);
    }

    // Read Task ID

    @Override
    public TaskResponse getTaskById(Long id) {
        log.debug("Fetching task with id: {}", id);
        Task task = findTaskOrThrow(id);
        return taskMapper.toResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        log.debug("Fetching all tasks");
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByStatus(Task.Status status) {
        log.debug("Fetching tasks with status: {}", status);
        return taskRepository.findByStatus(status)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByPriority(Task.Priority priority) {
        log.debug("Fetching tasks with priority: {}", priority);
        return taskRepository.findByPriority(priority)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> searchTasks(String keyword) {
        log.debug("Searching tasks with keyword: {}", keyword);
        return taskRepository.searchByKeyword(keyword)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // Update Task  with ID

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        log.info("Updating task id: {}", id);
        Task task = findTaskOrThrow(id);
        taskMapper.updateEntityFromRequest(task, request);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse patchStatus(Long id, Task.Status status) {
        log.info("Patching status of task id: {} to {}", id, status);
        Task task = findTaskOrThrow(id);
        task.setStatus(status);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    // Delete Task with ID

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("Deleting task id: {}", id);
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    // Overall Stats

    @Override
    public TaskStatsResponse getStats() {
        return TaskStatsResponse.builder()
                .total(taskRepository.count())
                .todo(taskRepository.countByStatus(Task.Status.TODO))
                .inProgress(taskRepository.countByStatus(Task.Status.IN_PROGRESS))
                .done(taskRepository.countByStatus(Task.Status.DONE))
                .cancelled(taskRepository.countByStatus(Task.Status.CANCELLED))
                .build();
    }

    // private helper

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}