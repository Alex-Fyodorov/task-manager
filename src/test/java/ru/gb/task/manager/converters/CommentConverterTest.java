package ru.gb.task.manager.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.services.TaskService;
import ru.gb.task.manager.services.UserService;

import java.util.Optional;

@SpringBootTest(classes = CommentConverter.class)
public class CommentConverterTest {
    @Autowired
    private CommentConverter commentConverter;
    @MockBean
    private TaskService taskService;
    @MockBean
    private UserService userService;
    private User user;
    private Task task;

    @BeforeEach
    public void init() {
        user = new User();
        user.setUsername("user");
        task = new Task();
        task.setId(1L);
    }

    @ParameterizedTest
    @DisplayName("Преобразование dto в сущность")
    @CsvSource({
            "1, user",
            "2, user",
            "1, user2"
    })
    public void dtoToEntityTest(Long taskId, String username) {
        Mockito.when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));
        CommentDto commentDto = new CommentDto(3L, "text", taskId, username);
        if (taskId != 1L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> commentConverter.dtoToEntity(commentDto),
                    String.format("Task not found. id: %d", commentDto.getTaskId()));
        } else if (!username.equals("user")) {
            Assertions.assertThrows(UserNotFoundException.class,
                    () -> commentConverter.dtoToEntity(commentDto),
                    String.format("User not found. Username: %s", commentDto.getAuthor()));
        } else {
            Comment comment = commentConverter.dtoToEntity(commentDto);
            assertions(comment, commentDto);
        }
    }

    @Test
    @DisplayName("Преобразование сущности в dto")
    public void entityToDtoTest() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setTask(task);
        comment.setAuthor(user);
        CommentDto commentDto = commentConverter.entityToDto(comment);
        assertions(comment, commentDto);
    }

    private void assertions(Comment comment, CommentDto commentDto) {
        Assertions.assertEquals(comment.getId(), commentDto.getId());
        Assertions.assertEquals(comment.getText(), commentDto.getText());
        Assertions.assertEquals(comment.getTask().getId(), commentDto.getTaskId());
        Assertions.assertEquals(comment.getAuthor().getUsername(), commentDto.getAuthor());
    }
}
