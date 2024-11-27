package ru.gb.task.manager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.task.manager.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
