package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDTO {

    private int id;
    private long time;

    @JsonProperty("post_id")
    private String postId;

    @JsonProperty("parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer parentId;

    @JsonProperty("author_id")
    private int authorId;

    @JsonProperty("comment_text")
    private String commentText;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

}
