package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private long dateFrom;
    private long dateTo;
    private List<String> tags = new ArrayList<>();

    public void setDate_from(long date_from) {
        this.dateFrom = date_from;
    }
    public void setDate_to(long dateTo) {
        this.dateTo = dateTo;
    }
}
