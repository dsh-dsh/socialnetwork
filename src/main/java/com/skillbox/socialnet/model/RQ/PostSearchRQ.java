package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.skillbox.socialnet.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostSearchRQ {

    private String author;
    private String text;
    private long dateFrom;
    private long dateTo;
    private List<String> tags;

    // FIXME @JsonProperty("date_from") @JsonProperty("date_to") не конвертирует
    //  нашел такую хитрость
    public void setDate_from(long date_from) {
        this.dateFrom = date_from;
    }
    public void setDate_to(long dateTo) {
        this.dateTo = dateTo;
    }
}
