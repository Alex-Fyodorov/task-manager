package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель статуса задачи")
public class StatusDto {

    @Schema(description = "ID данного статуса", example = "2")
    private Long id;
    @Schema(description = "Наименование статуса", example = "в процессе")
    private String title;
}
