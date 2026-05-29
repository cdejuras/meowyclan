package com.example.taskmanager;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskMapper;
import com.taskmanager.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private TaskMapper taskMapper;

	@InjectMocks
	private TaskServiceImpl taskService;

	private Task task;
	private TaskRequest request;
	private TaskResponse response;

	@BeforeEach
	void setUp() {
		task = Task.builder()
				.id(1L)
				.title("Test Task")
				.description("Test Description")
				.priority(Task.Priority.HIGH)
				.status(Task.Status.TODO)
				.build();

		request = new TaskRequest("Test Task", "Test Description",
				Task.Priority.HIGH, Task.Status.TODO);

		response = TaskResponse.builder()
				.id(1L)
				.title("Test Task")
				.description("Test Description")
				.priority(Task.Priority.HIGH)
				.status(Task.Status.TODO)
				.build();
	}

	@Test
	@DisplayName("createTask — should persist and return mapped response")
	void createTask_success() {
		when(taskMapper.toEntity(request)).thenReturn(task);
		when(taskRepository.save(task)).thenReturn(task);
		when(taskMapper.toResponse(task)).thenReturn(response);

		TaskResponse result = taskService.createTask(request);

		assertThat(result).isNotNull();
		assertThat(result.getTitle()).isEqualTo("Test Task");
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	@DisplayName("getTaskById — should return task when found")
	void getTaskById_found() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(taskMapper.toResponse(task)).thenReturn(response);

		TaskResponse result = taskService.getTaskById(1L);

		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("getTaskById — should throw TaskNotFoundException when not found")
	void getTaskById_notFound() {
		when(taskRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> taskService.getTaskById(99L))
				.isInstanceOf(TaskNotFoundException.class)
				.hasMessageContaining("99");
	}

	@Test
	@DisplayName("getAllTasks — should return list of responses")
	void getAllTasks_returnsList() {
		when(taskRepository.findAll()).thenReturn(List.of(task));
		when(taskMapper.toResponse(task)).thenReturn(response);

		List<TaskResponse> results = taskService.getAllTasks();

		assertThat(results).hasSize(1);
	}

	@Test
	@DisplayName("deleteTask — should call repository delete when task exists")
	void deleteTask_success() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

		taskService.deleteTask(1L);

		verify(taskRepository, times(1)).delete(task);
	}

	@Test
	@DisplayName("deleteTask — should throw when task not found")
	void deleteTask_notFound() {
		when(taskRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> taskService.deleteTask(99L))
				.isInstanceOf(TaskNotFoundException.class);

		verify(taskRepository, never()).delete(any());
	}

	@Test
	@DisplayName("patchStatus — should update status only")
	void patchStatus_success() {
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(taskRepository.save(task)).thenReturn(task);
		when(taskMapper.toResponse(task)).thenReturn(response);

		taskService.patchStatus(1L, Task.Status.DONE);

		assertThat(task.getStatus()).isEqualTo(Task.Status.DONE);
		verify(taskRepository, times(1)).save(task);
	}
}