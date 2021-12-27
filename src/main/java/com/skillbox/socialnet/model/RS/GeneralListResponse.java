package com.skillbox.socialnet.model.RS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.Calendar;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralListResponse<T> {
    private String error = "string";
    @JsonProperty("error_description")
    private String errorDesc;
    private Long timestamp;
    private Long total;
    private Integer offset;
    private Integer perPage;
    private List<T> data;

    public GeneralListResponse(List<T> data, Pageable pageable) {
        timestamp = Calendar.getInstance().getTimeInMillis();
        this.data = data;
        total = Long.valueOf(data.size());
        this.perPage = pageable.getPageSize();
        this.offset = pageable.getPageNumber();
    }
}