package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Semen V
 * @created 19|11|2021
 */

@Data
public class StatusUserDTO {

    @JsonProperty("user_id")
    private Integer userId;
    private String status;
}
