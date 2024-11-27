package ru.gb.task.manager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.task.manager.entities.Priority;

import java.util.Optional;

@Repository
public interface PriorityRepository extends CrudRepository<Priority, Long> {
    Optional<Priority> findByTitle(String title);
}
