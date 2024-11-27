package ru.gb.task.manager.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.gb.task.manager.entities.Comment;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Поиск комментариев по ID задачи")
    public void findByTaskId() {
        List<Comment> comments = commentRepository.findByTaskId(1L);
        Assertions.assertEquals(3, comments.size());
        Assertions.assertEquals("user3", comments.get(0).getAuthor().getUsername());
    }

    @Test
    @DisplayName("Удаление комментариев по ID задачи")
    public void deleteByTaskId() {
        commentRepository.deleteByTaskId(1L);
        List<Comment> comments = commentRepository.findByTaskId(1L);
        Assertions.assertEquals(0, comments.size());
    }
}
