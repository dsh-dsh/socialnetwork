package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import lombok.Data;

@Data
public class DialogDTO {

    private long id;
    @JsonProperty("last_message")
    private MessageDTO lastMessage;
    private PersonDialogDTO recipient;
    @JsonProperty("unread_count")
    private int unreadCount;
    @JsonProperty("message_count")
    private int messageCount;

    public DialogDTO(Dialog dialog, Person me, Message lastMessage, int unreadCount, int messageCount) {
        this.id = dialog.getId();
        this.lastMessage = new MessageDTO(me, lastMessage);
        Person person = dialog.getPersons().stream()
                .filter(p -> !p.equals(me))
                .findFirst()
                .orElseThrow(BadRequestException::new);
        this.recipient = new PersonDialogDTO(person);
        this.unreadCount = unreadCount;
        this.messageCount = messageCount;
    }
}