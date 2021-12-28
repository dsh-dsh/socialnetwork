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
//    private final DialogService dialogService;

    public Page<Message> getMessagesByDialog(Dialog dialog, Pageable pageable) {
        return  messageRepository.findByDialog(dialog, pageable);
    }

    public void deleteMessages(List<Message> messages) {
        messageRepository.deleteMessagesByList(messages);
    }

    public long countUnreadMessages(Set<Dialog> dialogs) {
        return dialogs.stream()
                .flatMap(dialog -> dialog.getMessages().stream())
                .filter(message -> message.getReadStatus().equals(MessageReadStatus.SENT))
                .count();
    }

    public Message addMessage(Dialog dialog, Person author, String text) {
//        Person recipient = dialogService.getRecipient(dialog, author);

        Person recipient = dialog.getPersons().stream()
                .filter(person -> !person.equals(author))
                .findFirst()
                .orElseThrow(BadRequestException::new);

        Message message = new Message();
        message.setDialog(dialog);
        message.setAuthor(author);
        message.setMessageText(text);
        message.setReadStatus(MessageReadStatus.SENT);
        message.setRecipient(recipient);
        message.setTime(LocalDateTime.now());
        messageRepository.save(message);
        System.out.println(message);
        return message;
    }

    public Message getLastMessage(Dialog dialog) {
        return messageRepository.findFirst1ByDialogOrderByTimeDesc(dialog)
                .orElse(null);
    }

    public int getUnreadCount(Dialog dialog) {
        return messageRepository.countByDialogAndReadStatus(dialog, MessageReadStatus.SENT);
    }
}
