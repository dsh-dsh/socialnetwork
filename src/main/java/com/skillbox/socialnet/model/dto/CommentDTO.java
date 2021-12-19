package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentDTO {

    private int id;
    private long time;

    @JsonProperty("post_id")
    private String postId;

    @JsonProperty("parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int parentId;

    @JsonProperty("comment_text")
    private String commentText;

    private CommentAuthorDTO author;

    private boolean blocked;

    private int likes;

    @JsonProperty("my_like")
    private int myLike;

    @JsonProperty("sub_comments")
    private List<String> subComments = new ArrayList<>();

}
