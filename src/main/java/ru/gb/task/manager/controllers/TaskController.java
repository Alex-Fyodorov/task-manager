package ru.gb.task.manager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.task.manager.converters.TaskConverter;
import ru.gb.task.manager.dtos.TaskDto;
import ru.gb.task.manager.entities.Task;
import ru.gb.task.manager.exceptions.AppError;
import ru.gb.task.manager.exceptions.FieldsValidationError;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.services.TaskService;
import ru.gb.task.manager.validators.TaskValidator;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Методы работы с задачами")
public class TaskController {
    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final TaskValidator taskValidator;

    @GetMapping("/id/{id}")
    @Operation(summary = "Запрос на получение задачи по ID",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Задача не найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public TaskDto findByID(@PathVariable @Parameter(
            description = "ID задачи", required = true) Long id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task not found. id: %d", id)));
        return taskConverter.entityToDto(task);
    }

    @GetMapping("/admin_view")
    @Operation(summary = "Запрос на получение страницы с задачами для администратора",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            })
    public Page<TaskDto> findAllForAdmin (
            @RequestParam(required = false) @Parameter(
                    description = "Фильтр по имени автора", required = false) String author,
            @RequestParam(required = false) @Parameter(
                    description = "Фильтр по имени исполнителя", required = false) String executor,
            @RequestParam(required = false, name = "title_part") @Parameter(
                    description = "Фильтр по части заголовка задачи", required = false) String titlePart,
            @RequestParam(required = false, name = "status_id") @Parameter(
                    description = "Фильтр по ID статуса", required = false) Long statusId,
            @RequestParam(required = false, name = "priority_id") @Parameter(
                    description = "Фильтр по ID приоритета", required = false) Long priorityId,
            @RequestParam(defaultValue = "1") @Parameter(
                    description = "Номер страницы", required = false) Integer page,
            @RequestParam(defaultValue = "id") @Parameter(
                    description = "Сортировка задач по ID, автору, исполнителю, статусу и т.д.", required = false) String sort) {
        return taskService.findAll(author, executor, titlePart, statusId, priorityId, page, sort)
                .map(taskConverter::entityToDto);
    }

    @GetMapping("/my")
    @Operation(summary = "Запрос на получение страницы с задачами для которых данный пользователь " +
            "является исполнителем", responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            })
    public Page<TaskDto> findAllForExecutor (
            @RequestAttribute @Parameter(
                    description = "Имя исполнителя", required = true) String username,
            @RequestParam(required = false) @Parameter(
                    description = "Фильтр по имени автора", required = false) String author,
            @RequestParam(required = false, name = "title_part") @Parameter(
                    description = "Фильтр по части заголовка задачи", required = false) String titlePart,
            @RequestParam(required = false, name = "status_id") @Parameter(
                    description = "Фильтр по ID статуса", required = false) Long statusId,
            @RequestParam(required = false, name = "priority_id") @Parameter(
                    description = "Фильтр по ID приоритета", required = false) Long priorityId,
            @RequestParam(defaultValue = "1") @Parameter(
                    description = "Номер страницы", required = false) Integer page,
            @RequestParam(defaultValue = "id") @Parameter(
                    description = "Сортировка задач по ID, автору, исполнителю, статусу и т.д.", required = false) String sort) {
        return taskService.findAll(author, username, titlePart, statusId, priorityId, page, sort)
                .map(taskConverter::entityToDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Запрос на создание новой задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно создана", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Не заполнено одно из полей или исполнитель с таким именем не был найден", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = FieldsValidationError.class))
                    )
            })
    public TaskDto addNewTask(@RequestBody @Parameter(
            description = "Данные создаваемой задачи", required = true) TaskDto taskDto,
                              @RequestAttribute @Parameter(
            description = "Имя автора", required = true) String username) {
        taskDto.setAuthor(username);
        taskValidator.validate(taskDto);
        Task task = taskConverter.dtoToEntity(taskDto);
        return taskConverter.entityToDto(taskService.saveTask(task));
    }

    @PutMapping
    @Operation(summary = "Запрос на изменение существующей задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно изменена", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskDto.class))
                    ),
                    @ApiResponse(
                            description = "Не заполнено одно из полей", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = FieldsValidationError.class))
                    ),
                    @ApiResponse(
                            description = "Изменяемая задача не была найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public TaskDto updateTask(@RequestBody @Parameter(
            description = "Данные изменяемой задачи", required = true) TaskDto taskDto,
                              @RequestAttribute @Parameter(
            description = "Имя автора", required = true) String username) {
        taskDto.setAuthor(username);
        taskValidator.validate(taskDto);
        Task task = taskConverter.dtoToEntity(taskDto);
        return taskConverter.entityToDto(taskService.updateTask(task));
    }

    @PatchMapping("/status")
    @Operation(summary = "Запрос на изменение статуса задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно изменена", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Статус не был найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public void changeStatus(@RequestParam(name = "task_id") @Parameter(
            description = "ID задачи", required = true) Long taskId,
                             @RequestParam(name = "status_id") @Parameter(
            description = "ID статуса", required = true) Long statusId) {
        taskService.changeStatus(taskId, statusId);
    }

    @PatchMapping("/priority")
    @Operation(summary = "Запрос на изменение приоритета задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно изменена", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Приоритет не был найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public void changePriority(@RequestParam(name = "task_id") @Parameter(
            description = "ID задачи", required = true) Long taskId,
                              @RequestParam(name = "priority_id") @Parameter(
            description = "ID приоритета", required = true) Long priorityId) {
        taskService.changePriority(taskId, priorityId);
    }

    @PatchMapping("/executor")
    @Operation(summary = "Запрос на изменение исполнителя задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно изменена", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Новый исполнитель не был найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public void changeExecutor(@RequestParam(name = "task_id") @Parameter(
            description = "ID задачи", required = true) Long taskId,
                               @RequestParam(name = "executor_id") @Parameter(
            description = "ID нового исполнителя", required = true) Long executorId) {
        taskService.changeExecutor(taskId, executorId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Запрос на удаление задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно удалена", responseCode = "200"
                    )
            })
    public void deleteById(@PathVariable @Parameter(
            description = "ID задачи", required = true) Long id) {
        taskService.deleteById(id);
    }
}
