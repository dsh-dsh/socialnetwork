package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DialogDTO {
    private int id;
    @JsonProperty("last_message")
    private MessageDTO lastMessage;
    private PersonDialogDTO recipient;
    @JsonProperty("unread_count")
    private int unreadCount;
}
