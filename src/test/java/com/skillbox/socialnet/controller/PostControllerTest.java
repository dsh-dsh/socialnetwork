package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.rq.CommentRQ;
import com.skillbox.socialnet.model.rq.PostChangeRQ;
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

import java.util.ArrayList;

import static com.skillbox.socialnet.util.Constants.*;
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
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String URL_PREFIX = "/api/v1/post/";
    private static final String COMMENT = "/comments";


    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPost() throws Exception {
        mockMvc.perform(get(URL_PREFIX + 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.post_text").value("test post text"));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editPost() throws Exception {
        PostChangeRQ postChangeRQ = createPost("test title changed", "valid changed text");
        mockMvc.perform(put(URL_PREFIX + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postChangeRQ)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.post_text").value("valid changed text"))
                .andExpect(jsonPath("$.data.title").value("test title changed"));

    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editPostUnauthorized() throws Exception {
        PostChangeRQ postChangeRQ = createPost("test title changed", "valid changed text");
        mockMvc.perform(put(URL_PREFIX + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postChangeRQ)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithUserDetails(P1_MAIL)
    void editPostWithNotValidTitleTest() throws Exception {
        PostChangeRQ postChangeRQ = createPost("te", "changed valid text");
        mockMvc.perform(put(URL_PREFIX + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NOT_VALID_TITLE_MESSAGE));

    }

    @Test
    @WithUserDetails(P1_MAIL)
    void editPostWithNotValidTextTest() throws Exception {
        PostChangeRQ postChangeRQ = createPost("title", "short text");
        mockMvc.perform(put(URL_PREFIX + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NOT_VALID_TEXT_MESSAGE));

    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deletePost() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("10"));
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deletePostUnauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + 10))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getComments() throws Exception {
        mockMvc.perform(get(URL_PREFIX + 10 + COMMENT))
                .andDo(print())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").exists())
                .andExpect(jsonPath("$.data.[3]").doesNotExist());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addComment() throws Exception {
        mockMvc.perform(post(URL_PREFIX + 10 + COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRQ(null, "test comment new"))))
                .andDo(print())
                .andExpect(jsonPath("$.data.post_id").value(10));
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addCommentUnauthorized() throws Exception {
        mockMvc.perform(post(URL_PREFIX + 10 + COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRQ(null, "test comment new"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBlankComment() throws Exception {
        mockMvc.perform(post(URL_PREFIX + 10 + COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRQ(null, ""))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(BLANK_COMMENT_MESSAGE));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editComment() throws Exception {
        mockMvc.perform(put(URL_PREFIX + 10 + COMMENT + "/" + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRQ(null, "test comment edited"))))
                .andDo(print())
                .andExpect(jsonPath("$.data.post_id").value(10))
                .andExpect(jsonPath("$.data.comment_text").value("test comment edited"));
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editCommentUnauthorized() throws Exception {
        mockMvc.perform(put(URL_PREFIX + 10 + COMMENT + "/" + 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentRQ(null, "test comment edited"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteComment() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + 10 + COMMENT + "/" + 11))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("10"));
    }

    @Test
    @Sql(value = "/sql/post/addPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/post/deletePost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCommentUnauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + 10 + COMMENT + "/" + 11))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    private PostChangeRQ createPost(String title, String text) {
        PostChangeRQ postChangeRQ = new PostChangeRQ();
        postChangeRQ.setPostText(text);
        postChangeRQ.setTitle(title);
        postChangeRQ.setTags(new ArrayList<>());
        return postChangeRQ;
    }
}
