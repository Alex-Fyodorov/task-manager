package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель запроса на получение токена")
public class JwtRequest {

    @Schema(description = "Имя пользователя", example = "user@gmail.com")
    private String email;
    @Schema(description = "Пароль пользователя", example = "sdfg65446fg23")
    private String password;
}
