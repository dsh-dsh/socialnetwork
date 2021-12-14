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
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.model.mapper.PostModelMapper;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonModelMapper personModelMapper;
    private final PostModelMapper postModelMapper;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostService postService;

    public DefaultRS<?> getUser() {
        String email = authService.getPersonFromSecurityContext().getEMail();
        UserDTO userDTO = personModelMapper.mapToUserDTO(personService.getPersonByEmail(email));
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> getUserById(int id) {
        UserDTO userDTO = personModelMapper.mapToUserDTO(personService.getPersonById(id));
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
        return DefaultRSMapper.of(new MessageDTO());
    }

    //TODO need to be tested
    public DefaultRS<?> getUserWall(int id, Pageable pageable) {
        Person person = personService.getPersonById(id);
        List<Post> posts = postRepository.findPostsByAuthor(person, pageable).getContent();
        return DefaultRSMapper.of(postService.getListDTOFromPostList(posts), pageable);
    }


    //TODO need to be tested (not sure about postMapper cause of Post have Person and not UserDTO)
    public DefaultRS<?> addPostToUserWall(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = new Post();
        Person person = personService.getPersonById(id);
        post.setAuthor(person);
        post.setTitle(postChangeRQ.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        post.setTime(new Timestamp(publishDate));
        postRepository.save(post);
        return DefaultRSMapper.of(postModelMapper.mapToPostDTO(post));
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
        return DefaultRSMapper.of(new MessageDTO());
    }

    public DefaultRS<?> unblockUser(int id) {
        Person person = personService.getPersonById(id);
        person.setBlocked(false);
        personRepository.save(person);
        return DefaultRSMapper.of(new MessageDTO());
    }




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
