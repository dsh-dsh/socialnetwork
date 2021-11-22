package com.skillbox.socialnet.model.RS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDataRS {
    private int id;

    @JsonProperty("type_id")
    private int typeId;

    @JsonProperty("sent_time")
    private long sentTime;

    @JsonProperty("entity_id")
    private int entityId;

    private String info;
}
