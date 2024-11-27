package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель комментария")
public class CommentDto {

    @Schema(description = "ID комментария", example = "1461324")
    private Long id;
    @Schema(description = "Текст комментария", example = "1")
    private String text;
    @Schema(description = "ID задачи, к которой относится данный комментарий", example = "165419")
    private Long taskId;
    @Schema(description = "Автор комментария", example = "Александр")
    private String author;
}
