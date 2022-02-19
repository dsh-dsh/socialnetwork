package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.dto.LocationDTO;
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

import static com.skillbox.socialnet.util.Constants.NOT_VALID_LOCAL_MESSAGE;
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
class PlatformControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String P1_MAIL = "p1@mail.ru";
    private static final String URL_PREFIX = "/api/v1/platform/";
    private static final String CITIES = "cities";
    private static final String COUNTRIES = "countries";

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/addCities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCities() throws Exception {
        mockMvc.perform(get(URL_PREFIX + CITIES))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].title").value("Moscow"))
                .andExpect(jsonPath("$.data.[1].title").value("St. Petersburg"))
                .andExpect(jsonPath("$.data.[2]").doesNotExist());

    }

    @Test
    void getCitiesUnauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + CITIES))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setCity() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("Тирасполь");
        mockMvc.perform(post(URL_PREFIX + CITIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setCityEmpty() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("T");
        mockMvc.perform(post(URL_PREFIX + CITIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NOT_VALID_LOCAL_MESSAGE));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setCountryEmpty() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("T");
        mockMvc.perform(post(URL_PREFIX + COUNTRIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NOT_VALID_LOCAL_MESSAGE));
    }

    @Test
    void setCityUnauthorized() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("Тирасполь");
        mockMvc.perform(post(URL_PREFIX + CITIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/addCities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCountry() throws Exception {
        mockMvc.perform(get(URL_PREFIX + COUNTRIES))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].title").value("Russia"))
                .andExpect(jsonPath("$.data.[1].title").value("Ukraine"))
                .andExpect(jsonPath("$.data.[2]").doesNotExist());

    }

    @Test
    void getCountryUnauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + COUNTRIES))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/deleteCities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void setCountry() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("Молдова");
        mockMvc.perform(post(URL_PREFIX + COUNTRIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }

    @Test
    void setCountryUnauthorized() throws Exception {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setTitle("Молдова");
        mockMvc.perform(post(URL_PREFIX + COUNTRIES)
                        .content(objectMapper.writeValueAsString(locationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/platform/addLang.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/platform/deleteLang.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getLang() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "languages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].title").value("Rus"))
                .andExpect(jsonPath("$.data.[1].title").value("Eng"))
                .andExpect(jsonPath("$.data.[2]").doesNotExist());

    }

    @Test
    void getLangUnauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "languages"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
