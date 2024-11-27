package ru.gb.task.manager.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.TaskDto;
import ru.gb.task.manager.exceptions.ValidationException;
import ru.gb.task.manager.services.PriorityService;
import ru.gb.task.manager.services.StatusService;
import ru.gb.task.manager.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskValidator {
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserService userService;

    public void validate(TaskDto taskDto) {
        List<String> errorMessages = new ArrayList<>();
        if (taskDto.getTitle() == null || taskDto.getTitle().isBlank()) {
            errorMessages.add("The title field is not filled in.");
        }
        if (taskDto.getDescription() == null || taskDto.getDescription().isBlank()) {
            errorMessages.add("The description field is not filled in.");
        }
        if (taskDto.getStatusTitle() == null || taskDto.getStatusTitle().isBlank()) {
            errorMessages.add("The status field is not filled in.");
        }
        if (statusService.findByTitle(taskDto.getStatusTitle()).isEmpty()) {
            errorMessages.add("The status field is entered incorrectly.");
        }
        if (taskDto.getPriorityTitle() == null || taskDto.getPriorityTitle().isBlank()) {
            errorMessages.add("The priority field is not filled in.");
        }
        if (priorityService.findByTitle(taskDto.getPriorityTitle()).isEmpty()) {
            errorMessages.add("The priority field is entered incorrectly.");
        }
        if (taskDto.getExecutor() == null || taskDto.getExecutor().isBlank()) {
            errorMessages.add("The executor field is not filled in.");
        }
        if (userService.findByUsername(taskDto.getExecutor()).isEmpty()) {
            errorMessages.add(String.format("The user with username: %s is not found.", taskDto.getExecutor()));
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
