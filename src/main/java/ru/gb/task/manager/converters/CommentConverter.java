package ru.gb.task.manager.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.services.TaskService;
import ru.gb.task.manager.services.UserService;

@Component
@RequiredArgsConstructor
public class CommentConverter {
    private final TaskService taskService;
    private final UserService userService;

    public Comment dtoToEntity(CommentDto commentDto) {
        User author = userService.findByUsername(commentDto.getAuthor())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found. Username: %s", commentDto.getAuthor())));
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        Task task = taskService.findById(commentDto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task not found. id: %d", commentDto.getTaskId())));
        comment.setTask(task);
        return comment;
    }

    public CommentDto entityToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getTask().getId(), comment.getAuthor().getUsername());
    }
}
