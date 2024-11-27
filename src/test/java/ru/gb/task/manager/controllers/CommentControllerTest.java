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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Получение списка комментариев по ID задачи")
    public void getByTaskIdTest() throws Exception {
        mockMvc.perform(get("/api/v1/comments/1")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].text").isString())
                .andExpect(jsonPath("$[2].author", is("user3")));
    }

    @Test
    @DisplayName("Создание нового комментария")
    public void addNewCommentTest() throws Exception {
        String jsonRequest = "{\"text\": \"new comments text\", \"taskId\": 1}";
        mockMvc.perform(post("/api/v1/comments")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.text", is("new comments text")))
                .andExpect(jsonPath("$.id", is(6)));
    }

    private String getJwtToken() throws Exception {
        String jsonRequest = "{\"email\": \"user1@gmail.com\", \"password\": \"100\"}";
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
}
