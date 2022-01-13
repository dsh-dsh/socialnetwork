package com.skillbox.socialnet.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.model.enums.PostPublishType;
import lombok.*;

import java.util.Calendar;
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
    private String type = "POSTED";

    public static PostDTO getPostDTO(Post post, List<Post2tag> tags, List<PostComment> comments) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTime(post.getTime().getTime());
        postDTO.setAuthor(UserDTO.getUserDTO(post.getAuthor()));
        postDTO.setTitle(post.getTitle());
        postDTO.setPostText(post.getPostText());
        postDTO.setBlocked(post.isBlocked());
        postDTO.setLikes(postDTO.getLikes());
        postDTO.setMyLike(postDTO.getMyLike());


        String[] tagNames = post.getTags().stream()
                .map(tag2post -> tag2post.getTag().getTag())
                .toArray(String[]::new);
        postDTO.setTags(tagNames);
//        postDTO.setTags(tags.stream().map(tag2post -> tag2post.getTag().getTag()).toArray(String[]::new));

        postDTO.setComments(comments.stream().map(CommentDTO::getCommentDTO).collect(Collectors.toList()));
        postDTO.setType(getPostType(post));
        return postDTO;
    }

    private static String getPostType(Post post) {
        long postTime = post.getTime().getTime();
        String postType;
        if(postTime < Calendar.getInstance().getTimeInMillis()) {
            postType = String.valueOf(PostPublishType.POSTED);
        } else {
            postType = String.valueOf(PostPublishType.QUEUED);
        }
        return postType;
    }
}

