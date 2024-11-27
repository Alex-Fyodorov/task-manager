package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Модель задачи")
public class TaskDto {

    @Schema(description = "ID задачи", example = "3213654")
    private Long id;
    @Schema(description = "Заголовок задачи", example = "Замена лампочки")
    private String title;
    @Schema(description = "Описание задачи", example = "Поменять лампочку в коридоре второго этажа")
    private String description;
    @Schema(description = "Статус задачи", example = "завершено")
    private String statusTitle;
    @Schema(description = "Приоритет задачи", example = "средний")
    private String priorityTitle;
    @Schema(description = "Комментарии к задаче")
    private List<CommentDto> comments;
    @Schema(description = "Автор задачи", example = "Михаил Борисович")
    private String author;
    @Schema(description = "Исполнитель задачи", example = "Николай")
    private String executor;

    public TaskDto(Long id, String title, String description, String statusTitle, String priorityTitle, String author, String executor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusTitle = statusTitle;
        this.priorityTitle = priorityTitle;
        this.author = author;
        this.executor = executor;
    }
}
