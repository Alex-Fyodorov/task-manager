package ru.gb.task.manager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.task.manager.entities.Status;

import java.util.Optional;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {
    Optional<Status> findByTitle(String title);
}
