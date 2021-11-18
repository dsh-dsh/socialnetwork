package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RQ.PostChangeRQ;
import com.skillbox.socialnet.model.RQ.UserChangeRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.dto.PostDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    public DefaultRS getUser(int id) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getUserDTO(id));
        return defaultRS;
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

    public DefaultRS getUserWall(int id, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        List<PostDTO> posts = getPosts(id);
        defaultRS.setData(posts);
        defaultRS.setTotal(posts.size());
        return defaultRS;
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

    public DefaultRS searchUsers(String firstName, String lastName, int ageFrom, int ageTo, int cityId, int offset, int itemPerPage) {
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setOffset(offset);
        defaultRS.setPerPage(itemPerPage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        List<UserDTO> users = searchUsersDTO(firstName, lastName, ageFrom, ageTo, cityId);
        defaultRS.setData(users);
        defaultRS.setTotal(users.size());
        return defaultRS;
    }

    private List<UserDTO> searchUsersDTO(String firstName, String lastName, int ageFrom, int ageTo, int cityId) {
        //search Users
        List<UserDTO> users = new ArrayList<>();
        return users;
    }

    public DefaultRS blockUser(int id) {
        getUser(id);//block him
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getMessage());
        return defaultRS;
    }

    public DefaultRS unblockUser(int id) {
        getUser(id);//unblock him
        DefaultRS defaultRS = new DefaultRS();
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(getMessage());
        return defaultRS;
    }
}
