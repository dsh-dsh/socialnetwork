package com.skillbox.socialnet.model.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Calendar;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse<T> {
    private String error = "string";
    private Long timestamp;
    private T data;

    public GeneralResponse(T data) {
        timestamp = Calendar.getInstance().getTimeInMillis();
        this.data = data;
    }
}
