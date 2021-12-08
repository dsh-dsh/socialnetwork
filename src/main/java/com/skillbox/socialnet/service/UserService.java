package com.skillbox.socialnet.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserSearchRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
//import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonModelMapper personModelMapper;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final AuthService authService;

    public DefaultRS<?> getUser() {
        String email = authService.getPersonFromSecurityContext().getEMail();
        UserDTO userDTO = personModelMapper.mapToUserDTO(personService.getPersonByEmail(email));
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> getUserById(int id) {
        UserDTO userDTO = personModelMapper.mapToUserDTO(personService.getPersonById(id));
        //token?
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> editUser(UserChangeRQ userChangeRQ) {
        String email = authService.getPersonFromSecurityContext().getEMail();
        UserDTO userDTO = personModelMapper.mapToUserDTO(personService.editPerson(email, userChangeRQ));
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> deleteUser() {
        String email = authService.getPersonFromSecurityContext().getEMail();
        personRepository.delete(personService.getPersonByEmail(email));
        return DefaultRSMapper.of(getMessage());
    }

    private MessageDTO getMessage() {
        return new MessageDTO();
    }

    public Object getUserWall(int id, Pageable pageable) {
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

    public DefaultRS<?> addPostToUserWall(int id, long publishDate, PostChangeRQ postChangeRQ) {
        //add post to userId
        PostDTO post = new PostDTO();
        post.setAuthor(getUserDTO(id));
        post.setTitle(post.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        return DefaultRSMapper.of(post);
    }

    public DefaultRS<?> searchUsers(UserSearchRQ userSearchRQ, Pageable pageable) {
        Date to = getDateTo(userSearchRQ);
        Date from = getDateFrom(userSearchRQ);
        Page<Person> personPage = personRepository.findBySearchRequest(
                userSearchRQ.getFirstName(), userSearchRQ.getLastName(),
                userSearchRQ.getCountry(), userSearchRQ.getCity(), from, to, pageable);
        List<UserDTO> users = personPage.stream()
                .map(personModelMapper::mapToUserDTO).collect(Collectors.toList());
        return DefaultRSMapper.of(users, personPage);
    }

    public DefaultRS<?> blockUser(int id) {
        Person person = personService.getPersonById(id);
        person.setBlocked(true);
        personRepository.save(person);
        return DefaultRSMapper.of(getMessage());
    }

    public DefaultRS<?> unblockUser(int id) {
        Person person = personService.getPersonById(id);
        person.setBlocked(false);
        personRepository.save(person);
        return DefaultRSMapper.of(getMessage());
    }

    private UserDTO getUserDTO(int id) {
        return new UserDTO();
    }

    private String savePhotoInCloud(File f) throws FileNotFoundException {
        String s = RandomString.make(6);
        Regions clientRegion = Regions.EU_CENTRAL_1;
        String bucketName = "jevaibucket/publicprefix";
        String fileObjKeyName = s + ".jpg";
        String fileName = s;

        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIAVAR2I7GKLP66SIHL", "W3dXfLlwvfj+E8ucH62wwgalYZufOXLwFx2yxWu+");
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, f);
        s3Client.putObject(request);
        return "https://jevaibucket.s3.eu-central-1.amazonaws.com/publicprefix/" + fileObjKeyName;
    }

    private Date getDateFrom(UserSearchRQ userSearchRQ) {
        int ageTo = userSearchRQ.getAgeTo() == 0 ? Constants.MAX_AGE : userSearchRQ.getAgeTo();
        Date from = Date.from(LocalDate.now().minusYears(ageTo).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return from;
    }

    private Date getDateTo(UserSearchRQ userSearchRQ) {
        int ageFrom = userSearchRQ.getAgeFrom()-1;
        Date to = Date.from(LocalDate.now().minusYears(ageFrom).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return to;
    }
}
