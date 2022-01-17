package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.service.UserService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        {"spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public",
                "spring.datasource.username=postgres",
                "spring.datasource.password=1488228"})
@SpringBootTest
@AutoConfigureMockMvc

public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    private static final String URL_PREFIX = "/api/v1/users/";
    private static final String ME = "me";
    private static final int ID_1 = 1;
    private static final String EXISTING_EMAIL = "nikita.r200019@gmail.com";
    private static final String FIRST_NAME = "Nick";
    private static final String LAST_NAME = "Jevai";
    private static final String ABOUT = "Some test info";

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void gettingPerson() throws Exception {
        mockMvc.perform(get(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(EXISTING_EMAIL));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void editingPerson() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.first_name").value(FIRST_NAME));
    }



    private UserChangeRQ getUserChangeRQ() {
        UserChangeRQ userChangeRQ = new UserChangeRQ();
        userChangeRQ.setAbout(ABOUT);
        userChangeRQ.setFirstName(FIRST_NAME);
        userChangeRQ.setLastName(LAST_NAME);
        userChangeRQ.setBirthDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        return userChangeRQ;
    }

}
