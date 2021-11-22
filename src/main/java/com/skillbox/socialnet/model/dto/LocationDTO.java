package com.skillbox.socialnet.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class LocationDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String title;

}
