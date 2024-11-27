package ru.gb.task.manager.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {

    @Query("select t from Task t left join fetch t.comments where t.id = :id")
    Optional<Task> findById(Long id);

    @Transactional
    @Modifying
    @Query("update Task t set t.status = :status where t.id = :taskId")
    void changeStatus(Long taskId, Status status);

    @Transactional
    @Modifying
    @Query("update Task t set t.priority = :priority where t.id = :taskId")
    void changePriority(Long taskId, Priority priority);

    @Transactional
    @Modifying
    @Query("update Task t set t.executor = :executor where t.id = :taskId")
    void changeExecutor(Long taskId, User executor);
}
