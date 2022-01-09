package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.service.AccountService;
import com.skillbox.socialnet.service.PersonService;
import com.skillbox.socialnet.util.Constants;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        {"spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public",
                "spring.datasource.username=postgres",
                "spring.datasource.password=123456"})
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String URL_PREFIX = "/api/v1/account";
    private static final String EXISTING_EMAIL = "dan.shipilov@gmail.com";
    private static final String TYPE_CODE = "FRIEND_REQUEST";
    private static final String NEW_EMAIL = "newuser@mail.ru";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String PASSWORD = "12345678";
    private static final String NEW_PASSWORD = "newPassword";
    private static final String ENCODED_PASSWORD = "$2y$12$NKArmf9agtEQw7rPDN4zb.rE90zeewGAUWNRkSrYW662FwL77NyCS";
    private static final int EXISTING_PERSON_ID = 6;

    private AccountRegisterRQ getRegisterRequest(String email) {
        AccountRegisterRQ request = new AccountRegisterRQ();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setEmail(email);
        request.setPasswd1(PASSWORD);
        request.setPasswd2(PASSWORD);
        return request;
    }

    @Test
    @Sql(statements = {"DELETE FROM person WHERE person.e_mail = '" + NEW_EMAIL + "'"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registerTest() throws Exception {
        AccountRegisterRQ request = getRegisterRequest(NEW_EMAIL);
        this.mockMvc.perform(
                        post(URL_PREFIX + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }

    @Test
    public void registerWithExistingEmailTest() throws Exception {
        AccountRegisterRQ request = getRegisterRequest(EXISTING_EMAIL);
        this.mockMvc.perform(
                        post(URL_PREFIX + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.EMAIL_EXISTS_MESSAGE));
    }

    @Test
    public void recoveryPasswordTest() throws Exception{
        AccountEmailRQ request = new AccountEmailRQ(NEW_EMAIL);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/password/recovery")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.BAD_REQUEST_MESSAGE));
    }

    @Test
    public void recoveryPasswordWithWrongEmailTest() throws Exception{
        AccountEmailRQ request = new AccountEmailRQ(EXISTING_EMAIL);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/password/recovery")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }

    @Test
    @Sql(statements =
            "UPDATE person " +
            "SET password = '" + ENCODED_PASSWORD + "', confirmation_code = '123456' " +
            "WHERE e_mail = '" + EXISTING_EMAIL + "'",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void passwordSetTest() throws Exception{
        String confirmationCode = accountService.getConfirmationCode(EXISTING_EMAIL);
        AccountPasswordSetRQ request = new AccountPasswordSetRQ(confirmationCode, NEW_PASSWORD);

        this.mockMvc.perform(
                        put(URL_PREFIX + "/password/set")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        Person person = personService.getPersonByEmail(EXISTING_EMAIL);
        assertTrue(passwordEncoder.matches(NEW_PASSWORD, person.getPassword()));
    }

    @Test
    public void passwordSetWithWrongCodeTest() throws Exception{
        AccountPasswordSetRQ request = new AccountPasswordSetRQ("wrongConfirmationCode", NEW_PASSWORD);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/password/set")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.BAD_REQUEST_MESSAGE));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    public void shiftEmailTest() throws Exception{
        AccountEmailRQ request = new AccountEmailRQ(NEW_EMAIL);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/shift-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(statements = "UPDATE person " +
            "SET e_mail = '" + EXISTING_EMAIL + "' " +
            "WHERE id = " + EXISTING_PERSON_ID,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setNewEmailTest() throws Exception{
        AccountEmailRQ request = new AccountEmailRQ(NEW_EMAIL);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertEquals(NEW_EMAIL, personService.getPersonById(EXISTING_PERSON_ID).getEMail());
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    public void setExistingEmailTest() throws Exception{
        AccountEmailRQ request = new AccountEmailRQ(EXISTING_EMAIL);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.EMAIL_EXISTS_MESSAGE));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(statements = "UPDATE notification_setting " +
            "SET permission = false " +
            "WHERE notification_type_code = '" + TYPE_CODE + "' " +
            "AND person_id = " + EXISTING_PERSON_ID,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void setNotificationTest() throws Exception{
        AccountNotificationRQ request = new AccountNotificationRQ(String.valueOf(NotificationTypeCode.FRIEND_REQUEST), true);
        this.mockMvc.perform(
                        put(URL_PREFIX + "/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));
    }
}
