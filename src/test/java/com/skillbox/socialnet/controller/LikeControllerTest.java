package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.rq.LikeRQ;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String P2_MAIL = "p2@mail.ru";
    private static final String URL_PREFIX = "/api/v1/";
    private static final String LIKES = "likes";

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getLikes() throws Exception {
        mockMvc.perform(get(URL_PREFIX + LIKES)
                        .param("item_id", "10")
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users.[0]").value("Василий Пупкин"));
    }

    @Test
    @WithUserDetails(P2_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addLikes() throws Exception {
        LikeRQ likeRQ = new LikeRQ(10, "type");
        mockMvc.perform(put(URL_PREFIX + LIKES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRQ)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users.[0]").value("Иван Иванов"));
    }

    @Test
    @WithUserDetails(P2_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addLikesWrongPost() throws Exception {
        LikeRQ likeRQ = new LikeRQ(100, "type");
        mockMvc.perform(put(URL_PREFIX + LIKES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addLikesUnauthorized() throws Exception {
        LikeRQ likeRQ = new LikeRQ(10, "type");
        mockMvc.perform(put(URL_PREFIX + LIKES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRQ)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getLiked() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "liked")
                        .param("item_id", "10")
                        .param("type", "type")
                        .param("user_id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.additionalProp1").value(true));
    }

    @Test
    @WithUserDetails(P2_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUnLiked() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "liked")
                        .param("item_id", "10")
                        .param("type", "type")
                        .param("user_id", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.additionalProp1").value(false));
    }

    @Test
    @WithUserDetails(P2_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUnLikedWrongPost() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "liked")
                        .param("item_id", "100")
                        .param("type", "type")
                        .param("user_id", "2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteLike() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "likes")
                        .param("item_id", "10")
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(P2_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteLikeWrongPost() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "likes")
                        .param("item_id", "100")
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteLikeUnauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "likes")
                        .param("item_id", "10")
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
