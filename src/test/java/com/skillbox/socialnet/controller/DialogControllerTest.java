package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.dto.MessageSendDtoRequest;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.service.DialogService;
import com.skillbox.socialnet.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        {"spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public",
                "spring.datasource.username=postgres",
                "spring.datasource.password=123456"})
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DialogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DialogService dialogService;
    @Autowired
    private PersonService personService;

    private static final String URL_PREFIX = "/api/v1/dialogs";

    private static final String EXISTING_EMAIL = "p1@mail.ru";
    private static final String RECIPIENT_EMAIL = "p4@mail.ru";
    private static final int NEW_DIALOG_RECIPIENT_ID = 4;

    private long newDialogId;

    @Test
    @Order(1)
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/dialog/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createDialogsTest() throws Exception {
        setTestingDialogId();
        DialogCreateDTORequest request = new DialogCreateDTORequest(List.of(NEW_DIALOG_RECIPIENT_ID));
        mockMvc.perform(post(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

        Person person = personService.getPersonByEmail(EXISTING_EMAIL);
        assertEquals(1, dialogService.getDialogs(person).size());
    }

    @Test
    @Order(2)
    @WithUserDetails(EXISTING_EMAIL)
    public void createDialogsExistsTest() throws Exception {
        DialogCreateDTORequest request = new DialogCreateDTORequest(List.of(NEW_DIALOG_RECIPIENT_ID));
        mockMvc.perform(post(URL_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

        Person person = personService.getPersonByEmail(EXISTING_EMAIL);
        assertEquals(1, dialogService.getDialogs(person).size());
    }

    @Test
    @Order(3)
    @WithUserDetails(EXISTING_EMAIL)
    public void getDialogsTest() throws Exception {
        setTestingDialogId();
        Pageable pageable = PageRequest.of(0, 10);
        mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(4)
    @RepeatedTest(2)
    @WithUserDetails(EXISTING_EMAIL)
    public void sendMessageTest() throws Exception {
        setTestingDialogId();
        MessageSendDtoRequest request = new MessageSendDtoRequest("message text");
        mockMvc.perform(post(URL_PREFIX + "/" + newDialogId + "/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message_text")
                        .value("message text"));
    }

    @Test
    @Order(7)
    @WithUserDetails(RECIPIENT_EMAIL)
    public void getRecipientUnreadCountTest() throws Exception {
        setTestingDialogId();
        mockMvc.perform(get(URL_PREFIX + "/unreaded"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(3));
    }

    @Test
    @Order(8)
    @WithUserDetails(EXISTING_EMAIL)
    public void getMessagesTest() throws Exception {
        setTestingDialogId();
        mockMvc.perform(get(URL_PREFIX + "/" + newDialogId + "/messages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(9)
    @WithUserDetails(RECIPIENT_EMAIL)
    public void getRecipientUnreadCountAfterGetMessageTest() throws Exception {
        setTestingDialogId();
        mockMvc.perform(get(URL_PREFIX + "/unreaded"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(0));
    }

    @Test
    @Order(10)
    @RepeatedTest(1)
    @WithUserDetails(RECIPIENT_EMAIL)
    public void sendReplyMessageTest() throws Exception {
        setTestingDialogId();
        MessageSendDtoRequest request = new MessageSendDtoRequest("message text");
        mockMvc.perform(post(URL_PREFIX + "/" + newDialogId + "/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message_text")
                        .value("message text"));
    }

    @Test
    @Order(11)
    @WithUserDetails(EXISTING_EMAIL)
    public void getAuthorUnreadCountTest() throws Exception {
        setTestingDialogId();
        mockMvc.perform(get(URL_PREFIX + "/unreaded"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(2));
    }

    private void setTestingDialogId() {
        Person author = personService.getPersonByEmail(EXISTING_EMAIL);
        Person recipient = personService.getPersonById(NEW_DIALOG_RECIPIENT_ID);
        Dialog dialog = dialogService.getDialogByPersonSet(author, Set.of(author, recipient)).orElse(null);
        if(dialog != null) {
            this.newDialogId = dialog.getId();
        }
    }
}
