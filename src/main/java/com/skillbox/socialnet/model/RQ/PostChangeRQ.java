package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostChangeRQ {

    private String title;
    @JsonProperty("post_text")
    private String postText;

}
