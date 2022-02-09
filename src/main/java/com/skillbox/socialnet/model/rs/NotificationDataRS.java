package com.skillbox.socialnet.model.rs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDataRS {
    @JsonProperty("entity_author")
    private CommentAuthorDTO entityAuthor;

    @JsonProperty("event_type")
    private String eventType;

    private int id;

    private String info;

    @JsonProperty("sent_time")
    private Timestamp sentTime;
}
