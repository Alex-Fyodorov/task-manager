package ru.gb.task.manager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.repositories.TaskRepository;

import java.util.Optional;

@SpringBootTest(classes = TaskService.class)
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;
    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private StatusService statusService;
    @MockBean
    private PriorityService priorityService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private UserService userService;

    @ParameterizedTest
    @DisplayName("Поиск всех задач")
    @CsvSource({
            "1, 0, 1, id",
            "0, 1, 1, id",
            "0, 0, 1, id"
    })
    public void findAllTest(Long statusId, Long priorityId, Integer page, String sort) {
        Mockito.when(taskRepository.findAll(Specification.where(null),
                        PageRequest.of(0, 5, Sort.by("id"))))
                .thenReturn(Page.empty());

        if (statusId != 0L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskService.findAll(null, null, null, statusId, null, page, sort),
                    String.format("Status not found. id: %d", statusId));
        } else if (priorityId != 0L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskService.findAll(null, null, null, null, priorityId, page, sort),
                    String.format("Priority not found. id: %d", priorityId));
        } else {
            Page<Task> taskPage = taskService.findAll(null, null, null, null, null, page, sort);
            Mockito.verify(taskRepository, Mockito.times(1))
                    .findAll(Mockito.eq(Specification.where(null)),
                            Mockito.eq(PageRequest.of(page - 1, 5, Sort.by(sort))));
        }
    }

    @Test
    @DisplayName("Сохранение задачи")
    public void saveTaskTest() {
        Task task = new Task();
        task.setId(1L);
        taskService.saveTask(task);

        Mockito.verify(taskRepository, Mockito.times(1))
                .save(ArgumentMatchers.any());
        Mockito.verify(taskRepository).save(Mockito.eq(task));
        Assertions.assertNull(task.getId());
    }

    @ParameterizedTest
    @DisplayName("Изменение задачи")
    @CsvSource({"1", "20"})
    public void updateTaskTest(Long taskId) {
        Task task = new Task();
        task.setId(taskId);

        Mockito.doReturn(Optional.of(task))
                .when(taskRepository)
                .findById(1L);

        if (taskId == 1L) {
            taskService.updateTask(task);
            Mockito.verify(taskRepository, Mockito.times(1))
                    .save(Mockito.eq(task));
        } else {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskService.updateTask(task),
                    String.format("Task not found. id: %d", task.getId()));
        }
    }

    @ParameterizedTest
    @DisplayName("Замена статуса")
    @CsvSource({"1, 2", "1, 20"})
    public void changeStatusTest(Long taskId, Long statusId) {
        Status status = new Status();
        status.setId(2L);
        status.setTitle("gotovo");

        Mockito.doReturn(Optional.of(status))
                .when(statusService)
                .findById(2L);

        if (statusId != 2L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskService.changeStatus(taskId, statusId),
                    String.format("Status not found. id: %d", statusId));
        } else {
            taskService.changeStatus(taskId, statusId);
            Mockito.verify(taskRepository, Mockito.times(1))
                    .changeStatus(Mockito.eq(taskId), Mockito.eq(status));
        }
    }

    @ParameterizedTest
    @DisplayName("Замена приоритета")
    @CsvSource({"1, 2", "1, 20"})
    public void changePriorityTest(Long taskId, Long priorityId) {
        Priority priority = new Priority();
        priority.setId(2L);
        priority.setTitle("sredniy");

        Mockito.doReturn(Optional.of(priority))
                .when(priorityService)
                .findById(2L);

        if (priorityId != 2L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskService.changePriority(taskId, priorityId),
                    String.format("Priority not found. id: %d", priorityId));
        } else {
            taskService.changePriority(taskId, priorityId);
            Mockito.verify(taskRepository, Mockito.times(1))
                    .changePriority(Mockito.eq(taskId), Mockito.eq(priority));
        }
    }

    @ParameterizedTest
    @DisplayName("Замена исполнителя")
    @CsvSource({"1, 2", "1, 20"})
    public void changeUserTest(Long taskId, Long userId) {
        User user = new User();
        user.setId(2L);
        user.setUsername("user2");

        Mockito.doReturn(Optional.of(user))
                .when(userService)
                .findById(2L);

        if (userId != 2L) {
            Assertions.assertThrows(UserNotFoundException.class,
                    () -> taskService.changeExecutor(taskId, userId),
                    String.format("User not found. id: %d", userId));
        } else {
            taskService.changeExecutor(taskId, userId);
            Mockito.verify(taskRepository, Mockito.times(1))
                    .changeExecutor(Mockito.eq(taskId), Mockito.eq(user));
        }
    }
}
