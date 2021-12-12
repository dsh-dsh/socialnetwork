package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Semen V
 * @created 19|11|2021
 */

@Data
@NoArgsConstructor
public class StatusUserDTO {

    @JsonProperty("user_id")
    private Integer userId;
    private String status = "FRIEND";

    public StatusUserDTO(Integer userId) {
        this.userId = userId;
    }
}
