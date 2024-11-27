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
import ru.gb.task.manager.dtos.StatusDto;
import ru.gb.task.manager.converters.StatusConverter;
import ru.gb.task.manager.services.StatusService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statuses")
@RequiredArgsConstructor
@Tag(name = "Статус задачи", description = "Методы работы со статусами")
public class StatusController {
    private final StatusService statusService;
    private final StatusConverter statusConverter;

    @GetMapping
    @Operation(summary = "Запрос на получение полного списка статусов",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            })
    public List<StatusDto> getAll() {
        return statusService.findAll().stream()
                .map(statusConverter::entityToDto).toList();
    }
}
