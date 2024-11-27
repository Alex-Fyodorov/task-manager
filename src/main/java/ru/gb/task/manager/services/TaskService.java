package ru.gb.task.manager.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.gb.task.manager.entities.Priority;
import ru.gb.task.manager.entities.Status;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.repositories.TaskRepository;
import ru.gb.task.manager.repositories.specifications.TaskSpecification;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final CommentService commentService;
    private final UserService userService;


    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Page<Task> findAll(String author, String executor, String titlePart, Long statusId,
                              Long priorityId, Integer page, String sort) {
        Specification<Task> specification = Specification.where(null);
        if (author != null) {
            specification = specification.and(TaskSpecification.authorEqualThan(author));
        }
        if (executor != null) {
            specification = specification.and(TaskSpecification.executorEqualThan(executor));
        }
        if (titlePart != null) {
            specification = specification.and(TaskSpecification.titleLikeOf(titlePart));
        }
        if (statusId != null) {
            Status status = statusService.findById(statusId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Status not found. id: %d", statusId)));
            specification = specification.and(TaskSpecification.statusEqualThan(status));
        }
        if (priorityId != null) {
            Priority priority = priorityService.findById(priorityId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Priority not found. id: %d", priorityId)));
            specification = specification.and(TaskSpecification.priorityEqualThan(priority));
        }
        if (page < 1) page = 1;
        Page<Task> taskPage = taskRepository.findAll(specification, PageRequest.of(page - 1, 5, Sort.by(sort)));
        taskPage.forEach(t -> t.setComments(commentService.findByTaskId(t.getId())));
        return taskPage;
    }

    public Task saveTask(Task task) {
        task.setId(null);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task updatedTask) {
        Task originalTask = findById(updatedTask.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task not found. id: %d", updatedTask.getId())));
        originalTask.setTitle(updatedTask.getTitle());
        originalTask.setDescription(updatedTask.getDescription());
        originalTask.setStatus(updatedTask.getStatus());
        originalTask.setPriority(updatedTask.getPriority());
        originalTask.setExecutor(updatedTask.getExecutor());
        return taskRepository.save(originalTask);
    }

    @Transactional
    public void changeStatus(Long taskId, Long statusId) {
        Status status = statusService.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status not found. id: %d", statusId)));
        taskRepository.changeStatus(taskId, status);
    }

    @Transactional
    public void changePriority(Long taskId, Long priorityId) {
        Priority priority = priorityService.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Priority not found. id: %d", priorityId)));
        taskRepository.changePriority(taskId, priority);
    }

    @Transactional
    public void changeExecutor(Long taskId, Long executorId) {
        User executor = userService.findById(executorId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found. id: %d", executorId)));
        taskRepository.changeExecutor(taskId, executor);
    }

    public void deleteById(Long id) {
        commentService.deleteByTaskId(id);
        taskRepository.deleteById(id);
    }
}
