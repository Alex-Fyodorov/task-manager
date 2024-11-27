package ru.gb.task.manager.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.exceptions.ValidationException;

@SpringBootTest(classes = CommentValidator.class)
public class CommentValidatorTest {
    @Autowired
    private CommentValidator commentValidator;

    @ParameterizedTest
    @DisplayName("Проверка валидирования новых комментариев")
    @CsvSource({
            "  , 0",
            "text, 0",
            "  , 1",
            "text, 1"
    })
    public void validateTest(String text, Long taskId) {
        CommentDto commentDto = new CommentDto();
        if (taskId != 0L) commentDto.setTaskId(taskId);
        commentDto.setText(text);
        if (commentDto.getTaskId() == null || commentDto.getText() == null || commentDto.getText().isBlank()) {
            Assertions.assertThrows(ValidationException.class,
                    () -> commentValidator.validate(commentDto));
        } else {
            Assertions.assertDoesNotThrow(() -> commentValidator.validate(commentDto));
        }
    }
}
