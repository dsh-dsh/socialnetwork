package com.skillbox.socialnet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.service.PersonService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties =
        "spring.datasource.url=jdbc:postgresql://localhost:5432/socialnettest?currentSchema=public")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private PersonService personService;

    private static final String URL_PREFIX = "/api/v1/friends";

    private static final String EMAIL_PERSON_ID1 = "p1@mail.ru";
    private static final String EMAIL_PERSON_ID3 = "p3@mail.ru";
    private static final String EMAIL_PERSON_ID5 = "p5@mail.ru";
    private static final int ONE_FRIEND_ID = 3;
    private static final int SRC_PERSON_ID1 = 1;
    private static final int DST_PERSON_ID1 = 1;
    private static final int DST_PERSON_ID5 = 5;

    @Order(1)
    @Test
    @Sql(value = "/sql/friend/before.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void setTestingData() throws Exception {
        //adding test data
    }

    @Order(2)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID5)
    void getFriendsOfPersonWithOneFriend() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].id").value(ONE_FRIEND_ID))
                .andExpect(jsonPath("$.data.[1]").doesNotExist());
    }

    @Order(3)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID3)
    void getFriendsOfPersonWithThreeFriends() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").exists())
                .andExpect(jsonPath("$.data.[2]").exists())
                .andExpect(jsonPath("$.data.[3]").doesNotExist());
    }

    @Order(4)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    void getNoRequests() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX + "/request"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.[0]").doesNotExist());
    }

    @Order(5)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID5)
    void addFriendRequest() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/" + DST_PERSON_ID1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertEquals(1, getRequestsNumber(EMAIL_PERSON_ID5, DST_PERSON_ID1));
    }

    @Order(6)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID5)
    void addFriendRequestIfExists() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/" + DST_PERSON_ID1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertEquals(1, getRequestsNumber(EMAIL_PERSON_ID5, DST_PERSON_ID1));
    }

    @Order(7)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    void getOneRequest() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX + "/request"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").doesNotExist());
    }

    @Order(8)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    void acceptFriendship() throws Exception {
        this.mockMvc.perform(post(URL_PREFIX + "/" + DST_PERSON_ID5))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("ok"));

        assertTrue(getFriendship(EMAIL_PERSON_ID1, DST_PERSON_ID5));
    }

    @Order(9)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    void getNoRequestAgain() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX + "/request"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.[0]").doesNotExist());
    }

    @Order(10)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    void getRecommendations() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX + "/recommendations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[1]").doesNotExist());
    }


    @Order(11)
    @Test
    @WithUserDetails(EMAIL_PERSON_ID1)
    @Sql(value = "/sql/friend/add_10_persons.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/friend/delete_new_persons.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
     void getRecommendationsAfterAdding10Persons() throws Exception {
        this.mockMvc.perform(get(URL_PREFIX + "/recommendations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.[0]").exists())
                .andExpect(jsonPath("$.data.[10]").exists())
                .andExpect(jsonPath("$.data.[11]").doesNotExist());
    }

    @Order(12)
    @Test
    @Sql(value = "/sql/friend/after.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteTestingData() throws Exception {
        //deleting test data
    }

    private int getRequestsNumber(String srcPersonEmail, int dstPersonId) {
        Person srcPerson = personService.getPersonByEmail(srcPersonEmail);
        Person dstPerson = personService.getPersonById(dstPersonId);
        List<Friendship> requests = friendshipRepository.findAllRequests(dstPerson, FriendshipStatusCode.REQUEST);
        return requests.size();
    }

    private boolean getFriendship(String srcPersonEmail, int dstPersonId) {
        Person srcPerson = personService.getPersonByEmail(srcPersonEmail);
        Person dstPerson = personService.getPersonById(dstPersonId);
        List<Friendship> friendships = friendshipRepository.findRequests(srcPerson, dstPerson, FriendshipStatusCode.FRIEND);
        return friendships.size() == 1;
    }
}
