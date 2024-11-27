package ru.gb.task.manager.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.gb.task.manager.entities.Role;
import ru.gb.task.manager.entities.User;
import ru.gb.task.manager.exceptions.ResourceNotFoundException;
import ru.gb.task.manager.exceptions.UserNotFoundException;
import ru.gb.task.manager.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleService roleService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    private User user;
    private Role role;

    @BeforeEach
    public void init() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setRoles(new ArrayList<>());

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user));
    }

    @ParameterizedTest
    @DisplayName("Поиск пользователя по имени")
    @CsvSource({"111", "222"})
    public void loadUserByUsernameTest(String email) {
        user.setEmail(email);
        user.setPassword("100");
        user.getRoles().add(role);
        Mockito.when(userRepository.findByEmail("111"))
                .thenReturn(Optional.of(user));
        if (email.equals("222")) {
            Assertions.assertThrows(UserNotFoundException.class,
                    () -> userService.loadUserByUsername(email),
                    String.format("User not found. email: %s", email));
        } else {
            UserDetails userDetails = userService.loadUserByUsername(email);
            Assertions.assertEquals(userDetails.getUsername(), email);
            Assertions.assertEquals(userDetails.getPassword(), "100");
            Assertions.assertEquals(userDetails.getAuthorities().size(), 1);
        }
    }

    @Test
    @DisplayName("Получение коллекции authorities")
    public void getAuthorityTest() {
        Collection<? extends GrantedAuthority> authorities = userService.getAuthority(new ArrayList<>(List.of(role)));
        Assertions.assertEquals(authorities.size(), 1);
        Assertions.assertEquals(authorities.stream()
                .filter(a -> a.getAuthority().equals("ROLE_USER"))
                .count(), 1);
    }

    @ParameterizedTest
    @DisplayName("Создание нового пользователя")
    @CsvSource({"1", "2"})
    public void saveNewUserTest(Long roleId) {
        user.setPassword("100");
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("$" + user.getPassword() + "$");
        Mockito.when(roleService.findById(roleId)).thenReturn(Optional.of(role));
        if (roleId == 2L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> userService.saveNewUser(user), "Role USER not found.");
        } else {
            userService.saveNewUser(user);
            Assertions.assertEquals(user.getRoles().get(0), role);
            Assertions.assertEquals(user.getPassword(), "$100$");
            Mockito.verify(userRepository, Mockito.times(1))
                    .save(Mockito.eq(user));
        }
    }

    @ParameterizedTest
    @DisplayName("Добавление роли пользователю")
    @CsvSource({"1, 2", "2, 1", "1, 1"})
    public void addRoleToUserTest(Long userId, Long roleId) {
        Mockito.when(roleService.findById(1L)).thenReturn(Optional.of(role));
        if (roleId != 1L) {
            Assertions.assertThrows(ResourceNotFoundException.class,
                    () -> userService.removeRoleFromUser(userId, roleId),
                    String.format("Role not found. id: %d", roleId));
        } else if (userId != 1L) {
            Assertions.assertThrows(UserNotFoundException.class,
                    () -> userService.removeRoleFromUser(userId, roleId),
                    String.format("User not found. id: %d", userId));
        } else {
            userService.addRoleToUser(userId, roleId);
            Assertions.assertEquals(user.getRoles().get(0), role);
        }
    }
}
