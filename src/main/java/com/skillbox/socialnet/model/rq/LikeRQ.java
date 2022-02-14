package com.skillbox.socialnet.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRQ {
    @JsonProperty("item_id")
    int id;
    String type;
}
