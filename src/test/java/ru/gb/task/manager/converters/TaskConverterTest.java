package ru.gb.task.manager.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.dtos.TaskDto;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.services.PriorityService;
import ru.gb.task.manager.services.StatusService;
import ru.gb.task.manager.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest(classes = TaskConverter.class)
public class TaskConverterTest {
    @Autowired
    private TaskConverter taskConverter;
    @MockBean
    private CommentConverter commentConverter;
    @MockBean
    private StatusService statusService;
    @MockBean
    private PriorityService priorityService;
    @MockBean
    private UserService userService;
    private Status status;
    private Priority priority;
    private User user;

    @BeforeEach
    public void init() {
        status = new Status();
        status.setTitle("status");
        priority = new Priority();
        priority.setTitle("priority");
        user = new User();
        user.setUsername("user");
    }

    @ParameterizedTest
    @DisplayName("Преобразование dto в сущность")
    @CsvSource({
            "1, any, anything, status, priority, user, user",
            "1, any, anything, status2, priority, user, user",
            "1, any, anything, status, priority2, user2, user",
            "1, any, anything, status, priority, user, user2"
    })
    public void dtoToEntityTest(Long id, String title, String description, String statusTitle,
                                String priorityTitle, String author, String executor) {
        Mockito.when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(statusService.findByTitle("status")).thenReturn(Optional.of(status));
        Mockito.when(priorityService.findByTitle("priority")).thenReturn(Optional.of(priority));
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setStatusTitle(statusTitle);
        taskDto.setPriorityTitle(priorityTitle);
        taskDto.setAuthor(author);
        taskDto.setExecutor(executor);
        taskDto.setComments(new ArrayList<>());

        if (statusTitle.equals("status2") || priorityTitle.equals("priority2")) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> taskConverter.dtoToEntity(taskDto));
        } else if (author.equals("user2") || executor.equals("user2")) {
            Assertions.assertThrows(UserNotFoundException.class,
                    () -> taskConverter.dtoToEntity(taskDto),
                    "User not found. Username: user2");
        } else {
            Task task = taskConverter.dtoToEntity(taskDto);
            assertions(task, taskDto);
        }
    }

    @Test
    @DisplayName("Преобразование сущности в dto")
    public void entityToDtoTest() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");
        task.setStatus(status);
        task.setPriority(priority);
        task.setAuthor(user);
        task.setExecutor(user);
        task.setComments(new ArrayList<>());
        Mockito.when(commentConverter.entityToDto(ArgumentMatchers.any()))
                .thenReturn(new CommentDto());
        TaskDto taskDto = taskConverter.entityToDto(task);
        assertions(task, taskDto);
    }

    private void assertions(Task task, TaskDto taskDto) {
        Assertions.assertEquals(task.getId(), taskDto.getId());
        Assertions.assertEquals(task.getTitle(), taskDto.getTitle());
        Assertions.assertEquals(task.getDescription(), taskDto.getDescription());
        Assertions.assertEquals(task.getStatus().getTitle(), taskDto.getStatusTitle());
        Assertions.assertEquals(task.getPriority().getTitle(), taskDto.getPriorityTitle());
        Assertions.assertEquals(task.getAuthor().getUsername(), taskDto.getAuthor());
        Assertions.assertEquals(task.getExecutor().getUsername(), taskDto.getExecutor());
    }
}
