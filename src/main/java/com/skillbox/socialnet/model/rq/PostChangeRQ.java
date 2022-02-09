package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostChangeRQ {


    @Size(min = 3, message = Constants.NOT_VALID_TITLE_MESSAGE)
    private String title;

    @Size(min = 15, message = Constants.NOT_VALID_TEXT_MESSAGE)
    @JsonProperty("post_text")
    private String postText;

    private List<String> tags;

}
