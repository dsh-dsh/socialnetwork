package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        {"spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public"})
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String URL_PREFIX = "/api/v1/notifications";

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/notification/addNotify.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/notification/deleteNotify.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getNotify() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").exists());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/notification/addNotify.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/notification/deleteNotify.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setNotify() throws Exception {
        mockMvc.perform(put(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").exists());
    }
}
