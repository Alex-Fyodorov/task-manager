package ru.gb.task.manager.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Доступ разрешён")
    public void securityAccessAllowedTest() throws Exception{
        String jsonRequest = "{\"email\": \"user2@gmail.com\", \"password\": \"200\"}";
        mockMvc.perform(post("/api/v1/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @DisplayName("Доступ запрещён")
    public void securityAccessDeniedTest() throws Exception{
        mockMvc.perform(get("/api/v1/statuses"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Доступ с пользователем")
    @WithMockUser(username = "user", roles = "USER")
    public void securityCheckUserTest() throws Exception{
        mockMvc.perform(get("/api/v1/statuses"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Админ-доступ с обычным пользователем")
    @WithMockUser(username = "user", roles = "USER")
    public void securityForbiddenTest() throws Exception{
        mockMvc.perform(get("/api/v1/tasks/admin_view"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Админ-доступ с администратором")
    @WithMockUser(username = "user", roles = "ADMIN")
    public void securityCheckAdminTest() throws Exception{
        mockMvc.perform(get("/api/v1/tasks/admin_view"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение токена")
    public void securityTokenTest() throws Exception{
        String jsonRequest = "{\"email\": \"user2@gmail.com\", \"password\": \"200\"}";
        MvcResult result = mockMvc.perform(post("/api/v1/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();
        token = token.replace("{\"token\":\"", "")
                .replace("\"}", "");

        mockMvc.perform(get("/api/v1/statuses")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
