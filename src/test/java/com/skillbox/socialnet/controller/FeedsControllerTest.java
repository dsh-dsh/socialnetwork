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
class FeedsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String URL_PREFIX = "/api/v1/feeds";

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getFeeds() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("string"));
    }

    @Test
    void getFeedsUnauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}
