package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель приоритета задачи")
public class PriorityDto {

    @Schema(description = "ID данного приоритета", example = "1")
    private Long id;
    @Schema(description = "Наименование приоритета", example = "высший")
    private String title;
}
