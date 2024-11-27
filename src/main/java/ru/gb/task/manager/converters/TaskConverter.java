package ru.gb.task.manager.converters;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskConverter {
    private final CommentConverter commentConverter;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserService userService;

    @Transactional
    public Task dtoToEntity(TaskDto taskDto) {
        Task task = new Task();
        Status status = statusService.findByTitle(taskDto.getStatusTitle())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status not found. Title: %s", taskDto.getStatusTitle())));
        Priority priority = priorityService.findByTitle(taskDto.getPriorityTitle())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Priority not found. Title: %s", taskDto.getPriorityTitle())));
        User author = userService.findByUsername(taskDto.getAuthor())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found. Username: %s", taskDto.getAuthor())));
        User executor = userService.findByUsername(taskDto.getExecutor())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found. Username: %s", taskDto.getExecutor())));

        task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(status);
        task.setPriority(priority);
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setComments(new ArrayList<>());
        return task;
    }

    public TaskDto entityToDto(Task task) {
        TaskDto taskDto = new TaskDto(task.getId(), task.getTitle(),
                task.getDescription(), task.getStatus().getTitle(),
                task.getPriority().getTitle(),
                task.getAuthor().getUsername(), task.getExecutor().getUsername());
        List<CommentDto> comments = task.getComments()
                .stream().map(commentConverter::entityToDto).toList();
        taskDto.setComments(comments);
        return taskDto;
    }
}
