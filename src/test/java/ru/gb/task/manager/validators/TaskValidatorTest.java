package ru.gb.task.manager.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.task.manager.dtos.TaskDto;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ValidationException;
import ru.gb.task.manager.services.PriorityService;
import ru.gb.task.manager.services.StatusService;
import ru.gb.task.manager.services.UserService;

import java.util.Optional;

@SpringBootTest(classes = TaskValidator.class)
public class TaskValidatorTest {
    @Autowired
    private TaskValidator taskValidator;
    @MockBean
    private StatusService statusService;
    @MockBean
    private PriorityService priorityService;
    @MockBean
    private UserService userService;

    @ParameterizedTest
    @DisplayName("Проверка валидирования новых задач")
    @CsvSource({
            "any, anything, gotovo, vysshiy, user",
            ", anything, gotovo, vysshiy, user",
            "any, , gotovo, vysshiy, user",
            "any, anything, , vysshiy, user",
            "any, anything, gotovo, , user",
            "any, anything, gotovo, vysshiy, ",
            "any, anything, gotovo2, vysshiy, user",
            "any, anything, gotovo, vysshiy2, user",
            "any, anything, gotovo, vysshiy, user2"
    })
    public void validateTest(String title, String description, String statusTitle,
                             String priorityTitle, String executor) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setStatusTitle(statusTitle);
        taskDto.setPriorityTitle(priorityTitle);
        taskDto.setExecutor(executor);
        Mockito.when(statusService.findByTitle("gotovo"))
                .thenReturn(Optional.of(new Status()));
        Mockito.when(priorityService.findByTitle("vysshiy"))
                .thenReturn(Optional.of(new Priority()));
        Mockito.when(userService.findByUsername("user"))
                .thenReturn(Optional.of(new User()));

        if (title == null || title.isBlank() || description == null || description.isBlank() ||
                statusTitle == null || statusTitle.isBlank() || priorityTitle == null || priorityTitle.isBlank() ||
                executor == null || executor.isBlank() || !statusTitle.equals("gotovo") ||
                !priorityTitle.equals("vysshiy") || !executor.equals("user")) {
            Assertions.assertThrows(ValidationException.class,
                    () -> taskValidator.validate(taskDto));
        } else {
            Assertions.assertDoesNotThrow(() -> taskValidator.validate(taskDto));
        }
    }
}
