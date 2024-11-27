package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель нового пользователя")
public class NewUserDto {

    @Schema(description = "Имя пользователя", example = "Сато Кейко")
    private String username;
    @Schema(description = "Пароль пользователя", example = "sdfg65446fg23")
    private String password;
    @Schema(description = "Email пользователя", example = "user@info.com")
    private String email;
}
