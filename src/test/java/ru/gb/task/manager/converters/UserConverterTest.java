package ru.gb.task.manager.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gb.task.manager.dtos.NewUserDto;
import ru.gb.task.manager.dtos.UserDto;
import ru.gb.task.manager.entities.User;

import java.util.ArrayList;

@SpringBootTest(classes = UserConverter.class)
public class UserConverterTest {
    @Autowired
    private UserConverter userConverter;

    @Test
    @DisplayName("Преобразование dto в сущность")
    public void newUserDtoToEntityTest() {
        NewUserDto userDto = new NewUserDto("user", "100", "111@111");
        User user = userConverter.newUserDtoToEntity(userDto);
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getPassword(), userDto.getPassword());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    @DisplayName("Преобразование сущности в dto")
    public void entityToDtoTest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("111@111");
        user.setRoles(new ArrayList<>());
        UserDto userDto = userConverter.entityToDto(user);
        Assertions.assertEquals(user.getUsername(), userDto.getUsername());
        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }
}
