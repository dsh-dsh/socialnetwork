package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRQ {

    @JsonProperty("parent_id")
    private Integer parentId;

    @NotBlank(message = Constants.BLANK_COMMENT_MESSAGE)
    @JsonProperty("comment_text")
    private String commentText;



}
