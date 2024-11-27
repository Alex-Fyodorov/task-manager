package ru.gb.task.manager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект для передачи токена")
public class JwtResponse {

    @Schema(description = "Токен", example = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidXNlcjEiLCJpYXQiOj" +
            "E3MjAwMDczMDksImV4cCI6MTcyMDAxMDkwOX0.JCUxbEYcsYPar_" +
            "8TTQDox4V8VQhHGWDsXRspX0qDOg4")
    private String token;
}
