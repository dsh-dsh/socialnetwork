package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.rq.AuthUserRQ;
import com.skillbox.socialnet.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        "spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL_PREFIX = "/api/v1/auth";

    private static final String EXISTING_EMAIL = "p1@mail.ru";
    private static final String PASSWORD = "12345678";
    private static final String NOT_EXISTING_EMAIL = "not.existing@email.com";
    private static final String WRONG_PASSWORD = "wrong_password";

    @Test
    void loginWithRightCredentialsTest() throws Exception {
        AuthUserRQ request = new AuthUserRQ(EXISTING_EMAIL, PASSWORD);
        mockMvc.perform(post(URL_PREFIX + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isString());
    }

    @Test
    void loginWithNotExistingUserTest() throws Exception {
        AuthUserRQ request = new AuthUserRQ(NOT_EXISTING_EMAIL, PASSWORD);
        mockMvc.perform(post(URL_PREFIX + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.NO_SUCH_USER_MESSAGE));
    }

    @Test
    void loginWithWrongPasswordTest() throws Exception {
        AuthUserRQ request = new AuthUserRQ(EXISTING_EMAIL, WRONG_PASSWORD);
        mockMvc.perform(post(URL_PREFIX + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Constants.WRONG_CREDENTIALS_MESSAGE));
    }

    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }
}
