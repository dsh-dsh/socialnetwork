package com.skillbox.socialnet.model.RQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DialogCreateDTORequest {
    @JsonProperty("user_ids")
    private List<Integer> userIds;
}
