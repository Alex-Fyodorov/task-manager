package ru.gb.task.manager.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.task.manager.dtos.NewUserDto;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ValidationException;
import ru.gb.task.manager.services.UserService;

import java.util.Optional;

@SpringBootTest(classes = UserValidator.class)
public class UserValidatorTest {
    @Autowired
    private UserValidator userValidator;
    @MockBean
    private UserService userService;

    @ParameterizedTest
    @DisplayName("Проверка валидирования новых пользователей")
    @CsvSource({
            "  , user, 111",
            "111@111, user, 111",
            "222@111, user, 111",
            "111@111, , 111",
            "111@111, user, "
    })
    public void validateTest(String email, String username, String password) {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(email);
        newUserDto.setUsername(username);
        newUserDto.setPassword(password);
        Mockito.when(userService.findByEmail("222@111")).thenReturn(Optional.of(new User()));
        if (email == null || email.isBlank() || username == null || username.isBlank() ||
                password == null || password.isBlank() || !email.equals("111@111")) {
            Assertions.assertThrows(ValidationException.class,
                    () -> userValidator.validate(newUserDto));
        } else {
            Assertions.assertDoesNotThrow(() -> userValidator.validate(newUserDto));
        }
    }
}
