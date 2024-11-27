package ru.gb.task.manager.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.task.manager.dtos.CommentDto;
import ru.gb.task.manager.converters.CommentConverter;
import ru.gb.task.manager.entities.Comment;
import ru.gb.task.manager.exceptions.AppError;
import ru.gb.task.manager.exceptions.FieldsValidationError;
import ru.gb.task.manager.services.CommentService;
import ru.gb.task.manager.validators.CommentValidator;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы работы с комментариями")
// http://localhost:8190/task-manager/swagger-ui/index.html
public class CommentController {
    private final CommentService commentService;
    private final CommentConverter commentConverter;
    private final CommentValidator commentValidator;

    @GetMapping("/{id}")
    @Operation(summary = "Запрос на получение списка комментариев по ID задачи",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            })
    public List<CommentDto> getByTaskId(@PathVariable @Parameter(
            description = "ID задачи", required = true) Long id) {
        return commentService.findByTaskId(id).stream()
                .map(commentConverter::entityToDto).collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Запрос на создание нового комментария",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CommentDto.class))
                    ),
                    @ApiResponse(
                            description = "Одно из полей не заполнено", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = FieldsValidationError.class))
                    ),
                    @ApiResponse(
                            description = "Задача не найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public CommentDto addNewComment(@RequestBody @Parameter(
            description = "Данные создаваемого комментария", required = true) CommentDto commentDto,
                                    @RequestAttribute @Parameter(
            description = "Имя автора комментария", required = true) String username) {
        commentValidator.validate(commentDto);
        commentDto.setAuthor(username);
        Comment comment = commentConverter.dtoToEntity(commentDto);
        return commentConverter.entityToDto(commentService.saveNewComment(comment));
    }
}
