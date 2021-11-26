package com.skillbox.socialnet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.User;
import com.skillbox.socialnet.model.enums.MessagesPermission;
import com.skillbox.socialnet.model.enums.UserType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    public Object getUser(int id) {
//        DefaultRS defaultRS = new DefaultRS();
//        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
//        defaultRS.setData(getUserDTO(id));
//        return defaultRS;
        String jsonStr = "{\n" +
                "  \"error\": \"string\",\n" +
                "  \"timestamp\": 1559751301818,\n" +
                "  \"data\": {\n" +
                "    \"id\": 1,\n" +
                "    \"first_name\": \"Петр\",\n" +
                "    \"last_name\": \"Петрович\",\n" +
                "    \"reg_date\": 1559751301818,\n" +
                "    \"birth_date\": 1559751301818,\n" +
                "    \"email\": \"petr@mail.ru\",\n" +
                "    \"phone\": \"89100000000\",\n" +
                "    \"photo\": \"https://avatanplus.com/files/resources/original/583a1361bea18158a2dbb5f5.png\",\n" +
                "    \"about\": \"Родился в небольшой, но честной семье\",\n" +
                "    \"city\": {\n" +
                "      \"id\": 1,\n" +
                "      \"title\": \"Москва\"\n" +
                "    },\n" +
                "    \"country\": {\n" +
                "      \"id\": 1,\n" +
                "      \"title\": \"Россия\"\n" +
                "    },\n" +
                "    \"messages_permission\": \"ALL\",\n" +
                "    \"last_online_time\": 1559751301818,\n" +
                "    \"is_blocked\": false\n" +
                "  }\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rootNode;
    }

    private UserDTO getUserDTO(int id) {
        return new UserDTO();
    }

    public DefaultRS editUser(int id, UserChangeRQ userChangeRQ) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getUserDTO(id));
        return defaultRS;
    }

    public DefaultRS deleteUser(int id) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getMessage());
        return defaultRS;
    }

    private MessageDTO getMessage() {
        return new MessageDTO();
    }

    public Object getUserWall(int id, int offset, int itemPerPage) {
//        DefaultRS defaultRS = new DefaultRS();
//        defaultRS.setOffset(offset);
//        defaultRS.setPerPage(itemPerPage);
//        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
//        List<PostDTO> posts = getPosts(id);
//        defaultRS.setData(posts);
//        defaultRS.setTotal(posts.size());
//        return defaultRS;
        String jsonStr = "{\n" +
                "  \"error\": \"string\",\n" +
                "  \"timestamp\": 1559751301818,\n" +
                "  \"total\": 0,\n" +
                "  \"offset\": 0,\n" +
                "  \"perPage\": 20,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"time\": 1559751301818,\n" +
                "      \"author\": {\n" +
                "        \"id\": 1,\n" +
                "        \"first_name\": \"Петр\",\n" +
                "        \"last_name\": \"Петрович\",\n" +
                "        \"reg_date\": 1559751301818,\n" +
                "        \"birth_date\": 1559751301818,\n" +
                "        \"email\": \"petr@mail.ru\",\n" +
                "        \"phone\": \"89100000000\",\n" +
                "        \"photo\": \"https://avatanplus.com/files/resources/original/583a1361bea18158a2dbb5f5.png\",\n" +
                "        \"about\": \"Родился в небольшой, но честной семье\",\n" +
                "        \"city\": {\n" +
                "          \"id\": 1,\n" +
                "          \"title\": \"Москва\"\n" +
                "        },\n" +
                "        \"country\": {\n" +
                "          \"id\": 1,\n" +
                "          \"title\": \"Россия\"\n" +
                "        },\n" +
                "        \"messages_permission\": \"ALL\",\n" +
                "        \"last_online_time\": 1559751301818,\n" +
                "        \"is_blocked\": false\n" +
                "      },\n" +
                "      \"title\": \"string\",\n" +
                "      \"post_text\": \"string\",\n" +
                "      \"is_blocked\": false,\n" +
                "      \"likes\": 23,\n" +
                "      \"comments\": [\n" +
                "        {\n" +
                "          \"parent_id\": 1,\n" +
                "          \"comment_text\": \"string\",\n" +
                "          \"id\": 111,\n" +
                "          \"post_id\": \"string\",\n" +
                "          \"time\": 1559751301818,\n" +
                "          \"author_id\": 1,\n" +
                "          \"is_blocked\": true\n" +
                "        }\n" +
                "      ],\n" +
                "      \"type\": \"POSTED\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rootNode;
    }

    private List<PostDTO> getPosts(int id) {
        //search post by userId
        List<PostDTO> posts = new ArrayList<>();
        return posts;
    }

    public DefaultRS addPostToUserWall(int id, long publishDate, PostChangeRQ postChangeRQ) {
        //add post to userId
        PostDTO post = new PostDTO();
        post.setAuthor(getUserDTO(id));
        post.setTitle(post.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(post);
        return defaultRS;
    }

    public Object searchUsers(String firstName, String lastName, int ageFrom, int ageTo, int cityId, int offset, int itemPerPage) {
//        DefaultRS defaultRS = new DefaultRS();
//        defaultRS.setOffset(offset);
//        defaultRS.setPerPage(itemPerPage);
//        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
//        List<UserDTO> users = searchUsersDTO(firstName, lastName, ageFrom, ageTo, cityId);
//        defaultRS.setData(users);
//        defaultRS.setTotal(users.size());
//        return defaultRS;
        String jsonStr = "{\n" +
                "  \"error\": \"string\",\n" +
                "  \"timestamp\": 1559751301818,\n" +
                "  \"total\": 0,\n" +
                "  \"offset\": 0,\n" +
                "  \"perPage\": 20,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"first_name\": \"Петр\",\n" +
                "      \"last_name\": \"Петрович\",\n" +
                "      \"reg_date\": 1559751301818,\n" +
                "      \"birth_date\": 1559751301818,\n" +
                "      \"email\": \"petr@mail.ru\",\n" +
                "      \"phone\": \"89100000000\",\n" +
                "      \"photo\": \"https://avatanplus.com/files/resources/original/583a1361bea18158a2dbb5f5.png\",\n" +
                "      \"about\": \"Родился в небольшой, но честной семье\",\n" +
                "      \"city\": {\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"Москва\"\n" +
                "      },\n" +
                "      \"country\": {\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"Россия\"\n" +
                "      },\n" +
                "      \"messages_permission\": \"ALL\",\n" +
                "      \"last_online_time\": 1559751301818,\n" +
                "      \"is_blocked\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"first_name\": \"Иван\",\n" +
                "      \"last_name\": \"Васильевич\",\n" +
                "      \"reg_date\": 1559751301818,\n" +
                "      \"birth_date\": 1559751301818,\n" +
                "      \"email\": \"ivan@mail.ru\",\n" +
                "      \"phone\": \"87600000000\",\n" +
                "      \"photo\": \"https://avatanplus.com/files/resources/original/583a1361bea18158a2dbb5f5.png\",\n" +
                "      \"about\": \"Родился в большой и доброй семье\",\n" +
                "      \"city\": {\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"Киев\"\n" +
                "      },\n" +
                "      \"country\": {\n" +
                "        \"id\": 1,\n" +
                "        \"title\": \"Украина\"\n" +
                "      },\n" +
                "      \"messages_permission\": \"ALL\",\n" +
                "      \"last_online_time\": 1559751301818,\n" +
                "      \"is_blocked\": false\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rootNode;
    }

    private List<UserDTO> searchUsersDTO(String firstName, String lastName, int ageFrom, int ageTo, int cityId) {
        //search Users
        List<UserDTO> users = new ArrayList<>();
        return users;
    }

    public DefaultRS blockUser(int id) {
        getUserDTO(id);//block him
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getMessage());
        return defaultRS;
    }

    public DefaultRS unblockUser(int id) {
        getUserDTO(id);//unblock him
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getMessage());
        return defaultRS;
    }
}
