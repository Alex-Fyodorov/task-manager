package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель пользователя")
public class UserDto {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "Сато Кейко")
    private String username;
    @Schema(description = "Email пользователя", example = "user@info.com")
    private String email;
    @Schema(description = "Список ролей пользователя", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private List<String> roles;
}
