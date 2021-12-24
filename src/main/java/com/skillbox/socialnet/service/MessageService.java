package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import com.skillbox.socialnet.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Page<Message> getMessagesByDialog(Dialog dialog, Pageable pageable) {
        return  messageRepository.findByDialog(dialog);
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

    public Message sendMessage(Dialog dialog, Person author, String text) {
        // TODO add message
        return new Message();
    }
}
