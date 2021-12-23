package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserSearchRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.CommentDTO;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
//import com.skillbox.socialnet.model.mapper.PersonModelMapper;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.model.mapper.PersonMapper;
import com.skillbox.socialnet.model.mapper.PostMapper;
import com.skillbox.socialnet.repository.*;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonMapper personMapper;
    private final PostMapper postMapper;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostService postService;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;

    public UserDTO getUser() {
        Person person = authService.getPersonFromSecurityContext();
        return UserDTO.getUserDTO(person);
    }

    public DefaultRS<?> getUserById(int id) {
        UserDTO userDTO = personMapper.mapToUserDTO(personService.getPersonById(id));
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> editUser(UserChangeRQ userChangeRQ) {
        String email = authService.getPersonFromSecurityContext().getEMail();
        UserDTO userDTO = personMapper.mapToUserDTO(personService.editPerson(email, userChangeRQ));
        return DefaultRSMapper.of(userDTO);
    }

    public DefaultRS<?> deleteUser() {
        String email = authService.getPersonFromSecurityContext().getEMail();
        personRepository.delete(personService.getPersonByEmail(email));
        return DefaultRSMapper.of(new MessageDTO());
    }


    public List<PostDTO> getUserWall(int id, Pageable pageable) {
        Person person = personService.getPersonById(id);
        Page<Post> postPage = postRepository.findPostsByAuthor(person, pageable);
        List<PostDTO> postDTOs = postPage.stream()
                .map(PostDTO::getPostDTO)
                .collect(Collectors.toList());
        return postDTOs;
    }



    public DefaultRS<?> addPostToUserWall(int id, long publishDate, PostChangeRQ postChangeRQ) {
        Post post = new Post();
        Person person = personService.getPersonById(id);
        post.setAuthor(person);
        post.setTitle(postChangeRQ.getTitle());
        post.setPostText(postChangeRQ.getPostText());
        post.setTime(new Timestamp((publishDate == 0) ? Calendar.getInstance().getTimeInMillis() : publishDate));
        post = postRepository.save(post);
        List<String> tagsList = postChangeRQ.getTags();
        if (tagsList.size() != 0) {
            checkTags(tagsList);
            addTags2Post(post, tagsList);
        }

        return DefaultRSMapper.of(postMapper.mapToPostDTO(post));
    }

    private void addTags2Post(Post post, List<String> tags){
        for (String tagName : tags) {
            Post2tag post2tag = new Post2tag();
            post2tag.setPost(post);
            post2tag.setTag(tagRepository.getTagByTag(tagName));
            tag2PostRepository.save(post2tag);
        }
    }
    private void checkTags(List<String> tags) {
        for (String tagName : tags) {
            if (tagRepository.getTagByTag(tagName) == null) {
                Tag tag = new Tag();
                tag.setTag(tagName);
                tagRepository.save(tag);
            }
        }
    }

    public DefaultRS<?> searchUsers(UserSearchRQ userSearchRQ, Pageable pageable) {
        Date to = getDateTo(userSearchRQ);
        Date from = getDateFrom(userSearchRQ);
        Page<Person> personPage = personRepository.findBySearchRequest(
                userSearchRQ.getFirstName(), userSearchRQ.getLastName(),
                userSearchRQ.getCountry(), userSearchRQ.getCity(), from, to, pageable);
        List<UserDTO> users = personPage.stream()
                .map(personMapper::mapToUserDTO).collect(Collectors.toList());
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


    private Date getDateFrom(UserSearchRQ userSearchRQ) {
        int ageTo = userSearchRQ.getAgeTo() == 0 ? Constants.MAX_AGE : userSearchRQ.getAgeTo();
        return Date.from(LocalDate.now().minusYears(ageTo).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getDateTo(UserSearchRQ userSearchRQ) {
        int ageFrom = userSearchRQ.getAgeFrom() - 1;
        return Date.from(LocalDate.now().minusYears(ageFrom).withDayOfYear(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
