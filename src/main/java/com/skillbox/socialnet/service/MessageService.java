package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import com.skillbox.socialnet.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Page<Message> getMessagePageByDialog(Dialog dialog, Pageable pageable) {
        return  messageRepository.findByDialog(dialog, pageable);
    }
    public List<Message> getMessagesByDialog(Dialog dialog) {
        return  messageRepository.findByDialog(dialog);
    }

    public long countUnreadMessages(Person me, Set<Dialog> dialogs) {
        return dialogs.stream()
                .flatMap(dialog -> dialog.getMessages().stream())
                .filter(message -> message.getRecipient().equals(me))
                .filter(message -> message.getReadStatus().equals(MessageReadStatus.SENT))
                .count();
    }

    public Message addMessage(Dialog dialog, Person author, Person recipient, String text) {
        Message message = new Message();
        message.setDialog(dialog);
        message.setAuthor(author);
        message.setMessageText(text);
        message.setReadStatus(MessageReadStatus.SENT);
        message.setRecipient(recipient);
        message.setTime(LocalDateTime.now());
        messageRepository.save(message);
        return message;
    }

    public Message getLastMessage(Dialog dialog) {
        return messageRepository.findFirst1ByDialogOrderByTimeDesc(dialog)
                .orElse(null);
    }

    public int getUnreadCount(Dialog dialog, Person recipient) {
        return messageRepository.countByDialogAndRecipientAndReadStatus(dialog, recipient, MessageReadStatus.SENT);
    }

    public void setMessagesStatusRead(List<Message> messages, Person recipient) {
        messageRepository.setMessagesReadStatus(messages, recipient, MessageReadStatus.READ);
    }

    //покаНеИспользуется
    public Message getMessageToRead(int messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(BadRequestException::new);
        return setMessagesStatusRead(message, MessageReadStatus.READ);
    }

    //покаНеИспользуется
    public Message setMessagesStatusRead(Message message, MessageReadStatus status) {
        message.setReadStatus(status);
        return messageRepository.save(message);
    }

    //покаНеИспользуется
    public void deleteMessages(List<Message> messages) {
        if(messages != null) {
            messageRepository.deleteMessagesByList(messages);
        }
    }
}
