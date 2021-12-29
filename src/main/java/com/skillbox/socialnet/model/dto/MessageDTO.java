package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import lombok.Data;

import java.time.ZoneId;

@Data
public class MessageDTO {
    private int id;
    private PersonDialogDTO author;
    @JsonProperty("isSentByMe")
    private boolean isSentByMe;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private MessageReadStatus readStatus;
    private PersonDialogDTO recipient;
    private long time;

    public MessageDTO(Person me, Message message) {
        if(message != null) {
            this.id = message.getId();
            this.author = new PersonDialogDTO(message.getAuthor());
            this.isSentByMe = message.getAuthor().equals(me);
            this.messageText = message.getMessageText();
            this.readStatus = message.getReadStatus();
            this.recipient = new PersonDialogDTO(message.getRecipient());
            this.time = message.getTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }
}
