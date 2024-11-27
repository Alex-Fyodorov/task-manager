package ru.gb.task.manager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> findByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment saveNewComment(Comment comment) {
        comment.setId(null);
        return commentRepository.save(comment);
    }

    public void deleteByTaskId(Long taskId) {
        commentRepository.deleteByTaskId(taskId);
    }
}
