package ru.gb.task.manager.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Поиск задачи по ID")
    public void findByIdTest() {
        Optional<Task> task = taskRepository.findById(1L);
        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(task.get().getComments().size(), 3);
        Assertions.assertEquals(task.get().getExecutor().getUsername(), "user3");
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    public void changeStatusTest() {
        Status status = new Status();
        status.setId(3L);
        taskRepository.changeStatus(1L, status);
        Optional<Task> task = taskRepository.findById(1L);
        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(task.get().getStatus().getTitle(), "завершено");
    }

    @Test
    @DisplayName("Изменение приоритета задачи")
    public void changePriorityTest() {
        Priority priority = new Priority();
        priority.setId(3L);
        taskRepository.changePriority(1L, priority);
        Optional<Task> task = taskRepository.findById(1L);
        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(task.get().getPriority().getTitle(), "низкий");
    }

    @Test
    @DisplayName("Изменение исполнителя задачи")
    public void changeExecutorTest() {
        User user = new User();
        user.setId(1L);
        taskRepository.changeExecutor(1L, user);
        Optional<Task> task = taskRepository.findById(1L);
        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(task.get().getExecutor().getUsername(), "user1");
    }
}
