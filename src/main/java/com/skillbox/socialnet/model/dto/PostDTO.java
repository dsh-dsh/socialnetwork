package com.skillbox.socialnet.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
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
    private List<CommentDTO> comments;
    private String[] tags;



}

