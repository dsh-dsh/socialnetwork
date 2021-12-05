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
import com.skillbox.socialnet.model.RQ.SearchRQ;
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
import com.skillbox.socialnet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import com.skillbox.socialnet.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

//    public Object searchUsers(SearchRQ searchRQ, Pageable pageable) {
//    }


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
}
