package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRQ {

    @JsonProperty("parent_id")
    private long parentId;
    @JsonProperty("comment_text")
    private String commentText;



}
