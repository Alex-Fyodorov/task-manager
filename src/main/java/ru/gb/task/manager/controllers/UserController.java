package ru.gb.task.manager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.task.manager.dtos.NewUserDto;
import ru.gb.task.manager.dtos.UserDto;
import ru.gb.task.manager.converters.UserConverter;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.AppError;
import ru.gb.task.manager.exceptions.FieldsValidationError;
import ru.gb.task.manager.services.UserService;
import ru.gb.task.manager.validators.UserValidator;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи", description = "Методы работы с пользователями")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final UserValidator userValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Запрос на создание нового пользователя",
            responses = {
                    @ApiResponse(
                            description = "Пользователь успешно создан", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            description = "Не заполнено одно из полей или пользователь с таким именем или email уже существует", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = FieldsValidationError.class))
                    )
            })
    public UserDto addNewUser(@RequestBody @Parameter(
            description = "Имя, email и пароль нового пользователя", required = true) NewUserDto newUserDto) {
        userValidator.validate(newUserDto);
        User user = userConverter.newUserDtoToEntity(newUserDto);
        return userConverter.entityToDto(userService.saveNewUser(user));
    }

    @PatchMapping("/add_role")
    @Operation(summary = "Запрос на добавление роли пользователю",
            responses = {
                    @ApiResponse(
                            description = "Роль пользователю успешно добавлена", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    ),
                    @ApiResponse(
                            description = "Роль не найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public void addRoleToUser(@RequestParam(name = "user_id") @Parameter(
            description = "ID пользователя", required = true) Long userId,
                              @RequestParam(name = "role_id") @Parameter(
            description = "ID роли", required = true) Long roleId) {
        userService.addRoleToUser(userId, roleId);
    }

    @PatchMapping("/remove_role")
    @Operation(summary = "Запрос на удаление роли у пользователя",
            responses = {
                    @ApiResponse(
                            description = "Роль успешно удалена", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    ),
                    @ApiResponse(
                            description = "Роль не найдена", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            })
    public void removeRoleFromUser(@RequestParam(name = "user_id") @Parameter(
            description = "ID пользователя", required = true) Long userId,
                              @RequestParam(name = "role_id") @Parameter(
            description = "ID роли", required = true) Long roleId) {
        userService.removeRoleFromUser(userId, roleId);
    }
}
