package ru.gb.task.manager.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String getJwtToken() throws Exception {
        String jsonRequest = "{\"email\": \"user2@gmail.com\", \"password\": \"200\"}";
        MvcResult result = mockMvc.perform(post("/api/v1/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();
        token = token.replace("{\"token\":\"", "")
                .replace("\"}", "");
        return token;
    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void addNewUserTest() throws Exception {
        String jsonRequest = "{\"email\": \"user5@gmail.com\", \"password\": \"500\", \"username\": \"user5\"}";
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.username", is("user5")));
    }

    @Test
    @DisplayName("Добавление роли пользователю")
    public void addRoleToUserTest() throws Exception {
        mockMvc.perform(patch("/api/v1/users/add_role")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .param("user_id", "1")
                        .param("role_id", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление роли у пользователя")
    public void removeRoleFromUserTest() throws Exception {
        mockMvc.perform(patch("/api/v1/users/remove_role")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .param("user_id", "1")
                        .param("role_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
