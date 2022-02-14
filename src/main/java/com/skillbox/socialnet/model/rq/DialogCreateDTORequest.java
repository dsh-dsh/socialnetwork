package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogCreateDTORequest {
    @JsonProperty("users_ids")
    private List<Integer> userIds;
}
