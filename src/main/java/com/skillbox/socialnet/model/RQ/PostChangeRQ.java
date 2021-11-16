package com.skillbox.socialnet.model.RQ;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostChangeRQ {

    private String title;
    private String post_text;

}
