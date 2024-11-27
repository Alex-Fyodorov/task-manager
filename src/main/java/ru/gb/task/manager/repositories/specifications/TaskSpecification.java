package ru.gb.task.manager.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;

public class TaskSpecification {

    public static Specification<Task> titleLikeOf(String titlePart) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart)));
    }

    public static Specification<Task> authorEqualThan(String author) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author").get("username"), author));
    }

    public static Specification<Task> executorEqualThan(String executor) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("executor").get("username"), executor));
    }

    public static Specification<Task> statusEqualThan(Status status) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Task> priorityEqualThan(Priority priority) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority));
    }
}
