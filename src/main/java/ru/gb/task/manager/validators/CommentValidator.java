package ru.gb.task.manager.validators;

import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentValidator {

    public void validate(CommentDto commentDto) {
        List<String> errorMessages = new ArrayList<>();
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            errorMessages.add("The comments body field is not filled in.");
        }
        if (commentDto.getTaskId() == null) {
            errorMessages.add("The task field is not filled in.");
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
