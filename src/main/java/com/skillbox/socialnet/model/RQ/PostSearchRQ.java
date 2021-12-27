package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Person;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostSearchRQ {

    private String author;
    private String text;
    @JsonProperty("date_from")
    private long date_from;  // FIXME @JsonProperty не конвертирует из httpQuery date_from в POJO dateFrom
    @JsonProperty("date_to")
    private long date_to;
    private List<String> tags;

}
