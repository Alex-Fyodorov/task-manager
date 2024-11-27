package ru.gb.task.manager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.task.manager.dtos.PriorityDto;
import ru.gb.task.manager.converters.PriorityConverter;
import ru.gb.task.manager.services.PriorityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/priorities")
@RequiredArgsConstructor
@Tag(name = "Приоритет задачи", description = "Методы работы с приоритетами")
public class PriorityController {
    private final PriorityService priorityService;
    private final PriorityConverter priorityConverter;

    @GetMapping
    @Operation(summary = "Запрос на получение полного списка приоритетов",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            })
    public List<PriorityDto> getAll() {
        return priorityService.findAll().stream()
                .map(priorityConverter::entityToDto).toList();
    }
}
