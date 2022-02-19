package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.service.Cloud;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        {"spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public"})
@SpringBootTest
@AutoConfigureMockMvc
class StorageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    Cloud cloud;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String URL_PREFIX = "/api/v1/storage";

    @Test
    @WithUserDetails(P1_MAIL)
    void saveImg() throws Exception {
        String name = "file";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        MockMultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, "content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_PREFIX)
                        .file(file)
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fileName").value(name));
    }

    @Test
    void saveImgUnauthorized() throws Exception {
        String name = "file";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        MockMultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, "content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_PREFIX)
                        .file(file)
                        .param("type", "type"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
