package ru.gb.task.manager.converters;

import org.springframework.stereotype.Component;
import ru.gb.task.manager.entities.Role;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.dtos.NewUserDto;
import ru.gb.task.manager.dtos.UserDto;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    public User newUserDtoToEntity(NewUserDto newUserDto) {
        User user = new User();
        user.setUsername(newUserDto.getUsername());
        user.setPassword(newUserDto.getPassword());
        user.setEmail(newUserDto.getEmail());
        return user;
    }

    public UserDto entityToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }
}
