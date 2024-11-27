package ru.gb.task.manager.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.entities.Task;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("select c from Comment c where c.task.id = :id")
    List<Comment> findByTaskId(Long id);

    @Modifying
    @Transactional
    @Query("delete from Comment c where c.task.id = :id")
    void deleteByTaskId(Long id);
}
