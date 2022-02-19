package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.model.rq.PostChangeRQ;
import com.skillbox.socialnet.model.rq.UserChangeRQ;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.service.FriendsService;
import com.skillbox.socialnet.service.PersonService;
import com.skillbox.socialnet.util.Constants;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        "spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public")
@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private FriendsService friendsService;


    private static final String URL_PREFIX = "/api/v1/users/";
    private static final String ME = "me";
    private static final int ID_1 = 1;
    private static final int ID_2 = 1;
    private static final String BLOCK = "block";
    private static final String EXISTING_EMAIL = "nikita.r200019@gmail.com";
    private static final String P1_MAIL = "p1@mail.ru";
    private static final String P2_MAIL = "p2@mail.ru";
    private static final String FIRST_NAME = "Nick";
    private static final String LAST_NAME = "Jevai";
    private static final String CITY = "City";
    private static final String COUNTRY = "Country";
    private static final String VALID_PHONE_NUMBER = "79001001010";
    private static final String NOT_VALID_PHONE_NUMBER = "notValidPhoneNumber";
    private static final Timestamp VALID_BIRTHDAY = Timestamp.valueOf("2010-10-10 00:00:00");
    private static final Timestamp BIRTHDAY_IN_FUTURE = Timestamp.valueOf("2030-01-01 00:00:00");
    private static final String ABOUT = "Some test info";

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void gettingPerson() throws Exception {
        mockMvc.perform(get(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(EXISTING_EMAIL));
    }


    @Test
    void gettingWrongPerson() throws Exception {
        mockMvc.perform(get(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPerson() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.first_name").value(FIRST_NAME));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPersonWithOutCityAndCountryTest() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        userChangeRQ.setCity(null);
        userChangeRQ.setCountry(null);
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.data.city").doesNotExist())
                .andExpect(jsonPath("$.data.country").doesNotExist());
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPersonWithOutFirstNameTest() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        userChangeRQ.setFirstName("");
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.WRONG_FIRST_NAME_MESSAGE));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPersonWithOutLastNameTest() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        userChangeRQ.setLastName("");
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.WRONG_LAST_NAME_MESSAGE));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPersonWithNotValidPhoneNumberTest() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        userChangeRQ.setPhone(NOT_VALID_PHONE_NUMBER);
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.NOT_VALID_PHONE_NUMBER_MESSAGE));
    }

    @Test
    @WithUserDetails(EXISTING_EMAIL)
    @Sql(value = "/sql/person/addPerson.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/person/deletePerson.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editingPersonWithNotValidBirthDateTest() throws Exception {
        UserChangeRQ userChangeRQ = getUserChangeRQ();
        userChangeRQ.setBirthDate(BIRTHDAY_IN_FUTURE);
        mockMvc.perform(put(URL_PREFIX + ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangeRQ)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.NOT_VALID_BIRTHDAY_MESSAGE));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    void getUserWall() throws Exception {
        mockMvc.perform(get(URL_PREFIX + ID_1 + "/wall"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].author.id").value(ID_1));
    }

    @Test
    @WithUserDetails(P2_MAIL)
    void searching() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "search")
                        .param("first_name", "Василий"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].first_name").value("Василий"));
    }

    @Test
    @WithUserDetails(P1_MAIL)
    @Sql(value = "/sql/person/deleteTestPost.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addPost() throws Exception {
        PostChangeRQ postChangeRQ = createPost("testTitle", "valid testText 1");
        mockMvc.perform(post(URL_PREFIX + ID_1 + "/wall")
                        .content(objectMapper.writeValueAsString(postChangeRQ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("testTitle"))
                .andExpect(jsonPath("$.data.post_text").value("valid testText 1"));

    }

    @Test
    void addPostUnauthorized() throws Exception {
        PostChangeRQ postChangeRQ = createPost("testTitle", "valid testText 1");
        mockMvc.perform(post(URL_PREFIX + ID_1 + "/wall")
                        .content(objectMapper.writeValueAsString(postChangeRQ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithUserDetails(P1_MAIL)
    void addPostWithNotValidTitleTest() throws Exception {
        PostChangeRQ postChangeRQ = createPost("te", "valid testText 1");
        mockMvc.perform(post(URL_PREFIX + ID_1 + "/wall")
                        .content(objectMapper.writeValueAsString(postChangeRQ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.NOT_VALID_TITLE_MESSAGE));

    }

    @Test
    @WithUserDetails(P1_MAIL)
    void addPostWithNotValidTextTest() throws Exception {
        PostChangeRQ postChangeRQ = createPost("title", "short text");
        mockMvc.perform(post(URL_PREFIX + ID_1 + "/wall")
                        .content(objectMapper.writeValueAsString(postChangeRQ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Constants.NOT_VALID_TEXT_MESSAGE));

    }

    @Test
    @WithUserDetails(P1_MAIL)
//    @Sql(value = "/sql/person/unblockP1.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(value = "/sql/person/unblockP1.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void blockPerson() throws Exception {
        mockMvc.perform(put(URL_PREFIX + BLOCK + "/" + ID_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertEquals(1, getBlockedRelationNumber(ID_1, ID_2));
    }

    @Test
    void blockPersonUnauthorized() throws Exception {
        mockMvc.perform(put(URL_PREFIX + BLOCK + "/" + ID_1))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(P1_MAIL)
//    @Sql(value = "/sql/person/blockP1.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(value = "/sql/person/unblockP1.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unblockPerson() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + BLOCK + "/" + ID_2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertEquals(0, getBlockedRelationNumber(ID_1, ID_2));
    }

    @Test
    void unblockPersonUnauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + BLOCK + "/" + ID_1))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUserUnauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + ME))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    private UserChangeRQ getUserChangeRQ() {
        UserChangeRQ userChangeRQ = new UserChangeRQ();
        userChangeRQ.setAbout(ABOUT);
        userChangeRQ.setFirstName(FIRST_NAME);
        userChangeRQ.setLastName(LAST_NAME);
        userChangeRQ.setPhone(VALID_PHONE_NUMBER);
        userChangeRQ.setBirthDate(VALID_BIRTHDAY);
        userChangeRQ.setCity(CITY);
        userChangeRQ.setCountry(COUNTRY);
        return userChangeRQ;
    }

    private PostChangeRQ createPost(String title, String text) {
        PostChangeRQ postChangeRQ = new PostChangeRQ();
        postChangeRQ.setPostText(text);
        postChangeRQ.setTitle(title);
        postChangeRQ.setTags(new ArrayList<>());
        return postChangeRQ;
    }



    private int getBlockedRelationNumber(int srcPersonId, int dstPersonId) {
        Person srcPerson = personService.getPersonById(srcPersonId);
        Person dstPerson = personService.getPersonById(dstPersonId);
        List<Friendship> requests = friendshipRepository.findRequests(srcPerson, dstPerson, FriendshipStatusCode.BLOCKED);
        return requests.size();
    }

}
