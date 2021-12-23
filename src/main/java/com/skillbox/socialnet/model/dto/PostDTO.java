package com.skillbox.socialnet.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.entity.Tag;
import com.skillbox.socialnet.repository.Tag2PostRepository;
import com.skillbox.socialnet.service.PostService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private int id;
    private long time;
    private UserDTO author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private int likes;
    @JsonProperty("my_like")
    private int myLike;
    private List<CommentDTO> comments;
    private String[] tags;
    private String type = "string";



    public static PostDTO getPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(postDTO.getId());
        postDTO.setTime(post.getTime().getTime());
        postDTO.setAuthor(UserDTO.getUserDTO(post.getAuthor()));
        postDTO.setTitle(postDTO.getTitle());
        postDTO.setPostText(post.getPostText());
        postDTO.setBlocked(postDTO.isBlocked());
        postDTO.setLikes(postDTO.getLikes());
        postDTO.setMyLike(postDTO.getMyLike());
//        postDTO.setTags(tag2PostRepository.getAllByPost(post).stream().map(Tag::getTag).collect(Collectors.toList()).toArray(String[]::new));
//        TODO add tags

        if (!post.getComments().isEmpty()) {
            postDTO.setComments(post.getComments().stream().map(CommentDTO::getCommentDTO).collect(Collectors.toList()));
        }
        return postDTO;
    }
}

