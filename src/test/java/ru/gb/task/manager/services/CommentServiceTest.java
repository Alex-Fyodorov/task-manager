package ru.gb.task.manager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.repositories.CommentRepository;

@SpringBootTest(classes = CommentService.class)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Создание нового комментария")
    public void saveNewCommentTest() {
        Comment comment = new Comment();
        comment.setId(80L);
        comment.setText("text");
        commentService.saveNewComment(comment);

        Mockito.verify(commentRepository, Mockito.times(1))
                .save(ArgumentMatchers.any());
        Mockito.verify(commentRepository).save(Mockito.eq(comment));
        Assertions.assertNull(comment.getId());
    }
}
