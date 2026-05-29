package com.taskmanager.config;

import com.taskmanager.entity.Task;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(TaskRepository taskRepository) {
        return args -> {
            if (taskRepository.count() == 0) {
                log.info("Seeding sample tasks...");

                List<Task> tasks = List.of(
                        Task.builder()
                                .title("Set up CI/CD pipeline")
                                .description("Configure GitHub Actions with build, test, and deploy stages.")
                                .priority(Task.Priority.HIGH)
                                .status(Task.Status.IN_PROGRESS)
                                .build(),
                        Task.builder()
                                .title("Design database schema")
                                .description("Create ERD and write migration scripts for the new user module.")
                                .priority(Task.Priority.CRITICAL)
                                .status(Task.Status.DONE)
                                .build(),
                        Task.builder()
                                .title("Write unit tests for service layer")
                                .description("Achieve at least 80% code coverage using JUnit 5 and Mockito.")
                                .priority(Task.Priority.MEDIUM)
                                .status(Task.Status.TODO)
                                .build(),
                        Task.builder()
                                .title("Fix pagination bug on dashboard")
                                .description("Page 2 returns duplicate records when sorted by date.")
                                .priority(Task.Priority.HIGH)
                                .status(Task.Status.TODO)
                                .build(),
                        Task.builder()
                                .title("Update README documentation")
                                .description("Add setup instructions and API usage examples.")
                                .priority(Task.Priority.LOW)
                                .status(Task.Status.TODO)
                                .build(),
                        Task.builder()
                                .title("Refactor authentication module")
                                .description("Extract JWT logic into a dedicated service class.")
                                .priority(Task.Priority.MEDIUM)
                                .status(Task.Status.IN_PROGRESS)
                                .build(),
                        Task.builder()
                                .title("Migrate from MySQL to PostgreSQL")
                                .description("Update JDBC driver, queries, and deployment configs.")
                                .priority(Task.Priority.CRITICAL)
                                .status(Task.Status.CANCELLED)
                                .build()
                );

                taskRepository.saveAll(tasks);
                log.info("Seeded {} tasks.", tasks.size());
            }
        };
    }
}