package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import lombok.Data;

@Data
public class MessageDTO {
    private int id;
    private PersonDialogDTO author;
    @JsonProperty("is_sent_by_me")
    private boolean isSentByMe;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private MessageReadStatus readStatus;
    private PersonDialogDTO recipient;
    private long time;

    public MessageDTO(Message message) {
        // TODO mapper
//        this.id = message.getId();
//        this.author = message.getAuthor()
//        this.isSentByMe = isSentByMe;
//        this.messageText = messageText;
//        this.readStatus = readStatus;
//        this.recipient = recipient;
//        this.time = time;
    }
}
