package ru.gb.task.manager.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gb.task.manager.dtos.NewUserDto;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ValidationException;
import ru.gb.task.manager.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserService userService;

    public void validate(NewUserDto newUserDto) {
        List<String> errorMessages = new ArrayList<>();
        Optional<User> user = userService.findByEmail(newUserDto.getEmail());
        if (user.isPresent()) {
            errorMessages.add(String.format("A user with email: %s is already exists.", newUserDto.getEmail()));
        }
        if (newUserDto.getUsername() == null || newUserDto.getUsername().isBlank()) {
            errorMessages.add("The username field is not filled in.");
        }
        if (newUserDto.getPassword() == null || newUserDto.getPassword().isBlank()) {
            errorMessages.add("The password field is not filled in.");
        }
        if (newUserDto.getEmail() == null || newUserDto.getEmail().isBlank()) {
            errorMessages.add("The email field is not filled in.");
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
