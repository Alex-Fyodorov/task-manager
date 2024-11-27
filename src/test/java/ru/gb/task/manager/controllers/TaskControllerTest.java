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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String getJwtToken(String email, String password) throws Exception {
        String jsonRequest = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
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
    @DisplayName("Получение задачи по ID")
    public void findByIDTest() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/id/1")
                        .header("Authorization", "Bearer " +
                                getJwtToken("user2@gmail.com", "200"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.title", is("лампочка")));
    }

    @Test
    @DisplayName("Получение страницы с задачами для администратора")
    public void findAllForAdminTest() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/admin_view")
                        .header("Authorization", "Bearer " +
                                getJwtToken("user2@gmail.com", "200"))
                        .param("priority_id", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.size", is(5)));
    }

    @Test
    @DisplayName("Получение страницы с задачами для исполнителя")
    public void findAllForExecutorTest() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/my")
                        .header("Authorization", "Bearer " +
                                getJwtToken("user1@gmail.com", "100"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(2)))
                .andExpect(jsonPath("$.size", is(5)));
    }

    @Test
    @DisplayName("Создание новой задачи")
    public void addNewTaskTest() throws Exception {
        String jsonRequest = "{\"title\": \"any\", \"description\": \"anything\", " +
                "\"statusTitle\": \"в ожидании\", \"priorityTitle\": \"высокий\", \"executor\": \"user1\"}";
        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " +
                                getJwtToken("user2@gmail.com", "200"))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.author", is("user2")))
                .andExpect(jsonPath("$.executor", is("user1")));
    }

    @Test
    @DisplayName("Изменение существующей задачи")
    public void updateTaskTest() throws Exception {
        String jsonRequest = "{\"id\": 1, \"title\": \"any\", \"description\": \"anything\", " +
                "\"statusTitle\": \"в ожидании\", \"priorityTitle\": \"высокий\", \"executor\": \"user1\"}";
        mockMvc.perform(put("/api/v1/tasks")
                        .header("Authorization", "Bearer " +
                                getJwtToken("user2@gmail.com", "200"))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("any")))
                .andExpect(jsonPath("$.executor", is("user1")));
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    public void changeStatusTest() throws Exception {
        String token = getJwtToken("user2@gmail.com", "200");
        mockMvc.perform(patch("/api/v1/tasks/status")
                        .header("Authorization", "Bearer " + token)
                        .param("task_id", "1")
                        .param("status_id", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks/id/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.statusTitle", is("в процессе")));
    }

    @Test
    @DisplayName("Изменение приоритета задачи")
    public void changePriorityTest() throws Exception {
        String token = getJwtToken("user2@gmail.com", "200");
        mockMvc.perform(patch("/api/v1/tasks/priority")
                        .header("Authorization", "Bearer " + token)
                        .param("task_id", "1")
                        .param("priority_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks/id/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.priorityTitle", is("высокий")));
    }

    @Test
    @DisplayName("Изменение исполнителя задачи")
    public void changeExecutorTest() throws Exception {
        String token = getJwtToken("user2@gmail.com", "200");
        mockMvc.perform(patch("/api/v1/tasks/executor")
                        .header("Authorization", "Bearer " + token)
                        .param("task_id", "1")
                        .param("executor_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks/id/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.executor", is("user1")));
    }

    @Test
    @DisplayName("Удаление задачи")
    public void deleteByIdTest() throws Exception {
        String token = getJwtToken("user2@gmail.com", "200");
        mockMvc.perform(delete("/api/v1/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tasks/id/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
