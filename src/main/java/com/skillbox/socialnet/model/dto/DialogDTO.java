package com.skillbox.socialnet.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public DialogDTO(Dialog dialog, Person me, Message lastMessage, int unreadCount) {
        this.id = dialog.getId();
        this.lastMessage = new MessageDTO(lastMessage);
        Person recipient = dialog.getPersons().stream()
                .filter(person -> !person.equals(me)).findFirst().get();
        this.recipient = new PersonDialogDTO(recipient);
        this.unreadCount = unreadCount;
    }
}
