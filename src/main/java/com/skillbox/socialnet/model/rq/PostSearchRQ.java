package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostSearchRQ {

    private String author;
    private String text;
    @JsonProperty("date_from")
    private long dateFrom;
    @JsonProperty("date_to")
    private long dateTo;
    private List<String> tags = new ArrayList<>();
}
