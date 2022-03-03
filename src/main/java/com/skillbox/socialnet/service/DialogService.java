package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.rq.DialogCreateDTORequest;
import com.skillbox.socialnet.model.rs.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.DialogRepository;
import com.skillbox.socialnet.util.ElementPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final DialogRepository dialogRepository;
    private final MessageService messageService;
    private final PersonService personService;
    private final AuthService authService;
    private final WebSocketService webSocketService;

    private static final Comparator<DialogDTO> comparatorByLastMessage =
            Comparator.comparing(dialogDTO -> dialogDTO.getLastMessage().getTime());

    public List<DialogDTO> getDialogs() {
        Person author = authService.getPersonFromSecurityContext();
        List<Dialog> dialogs = dialogRepository.findByPerson(author);

        return getDialogDTOList(author, dialogs);
    }

    public List<Dialog> getDialogs(Person author) {
        return dialogRepository.findByPerson(author);
    }

    @Transactional
    public DialogIdDTO createDialog(DialogCreateDTORequest dialogCreateDTORequest) {
        Set<Person> persons = personService.getPersonsByIdList(dialogCreateDTORequest.getUserIds());
        Person author = authService.getPersonFromSecurityContext();
        persons.add(author);
        Dialog dialog = getDialogByPersonSet(author, persons)
                .orElseGet(() -> addNewDialogToDataBase(persons));

        return new DialogIdDTO(dialog.getId());
    }

    public GeneralListResponse<MessageDTO> getMessagesInDialog(long dialogId, ElementPageable pageable) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Person author = authService.getPersonFromSecurityContext();
        Page<Message> messagePage = messageService.getMessagePageByDialog(dialog, pageable);
        messageService.setMessagesStatusRead(messagePage.getContent(), author);
        List<MessageDTO> messages = getMessageDTOList(author, messagePage.getContent());

        return new GeneralListResponse<>(messages, messagePage);
    }

    public UnreadCountDTO getUnreadCount() {
        Person author = authService.getPersonFromSecurityContext();
        Set<Dialog> dialogs = author.getDialogs();
        int unreadCount = (int) messageService.countUnreadMessages(author, dialogs);

        return new UnreadCountDTO(unreadCount);
    }

    public MessageDTO sendMessage(long dialogId, MessageSendDtoRequest messageSendDtoRequest) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Person author = authService.getPersonFromSecurityContext();
        Person recipient = getRecipient(dialog, author);
        Message message = messageService.addMessage(dialog, author,
                recipient, messageSendDtoRequest.getMessageText());

        pushMessageToFront(author, recipient, message);

        return new MessageDTO(author, message);
    }

    private void pushMessageToFront(Person author, Person recipient, Message message) {
        int recipientId = recipient.getId();
        webSocketService.sendMessage(recipientId, new MessageDTO(author, message));
        webSocketService.sendUnreadCount(recipientId);
    }

    private List<DialogDTO> getDialogDTOList(Person author, Collection<Dialog> dialogs) {
        return dialogs.stream()
                .map(dialog -> new DialogDTO(
                        dialog, author,
                        messageService.getLastMessage(dialog),
                        messageService.getUnreadCount(dialog, author),
                        messageService.getMessageCount(dialog)))
                .sorted(comparatorByLastMessage.reversed())
                .collect(Collectors.toList());
    }

    private List<MessageDTO> getMessageDTOList(Person author, Collection<Message> messages) {
        return messages.stream()
                .map(message -> new MessageDTO(author, message))
                .collect(Collectors.toList());
    }

    private Person getRecipient(Dialog dialog, Person author) {
        return dialog.getPersons().stream()
                .filter(person -> !person.equals(author))
                .findFirst()
                .orElseThrow(BadRequestException::new);
    }

    @Transactional
    public Optional<Dialog> getDialogByPersonSet(Person me, Set<Person> persons) {
        List<Dialog> dialogs = dialogRepository.findByPerson(me);
        return dialogs.stream()
                .filter(dialog -> dialog.getPersons().containsAll(persons))
                .findFirst();
    }

    private Dialog addNewDialogToDataBase(Set<Person> persons) {
        Dialog dialog = new Dialog(persons);
        return dialogRepository.save(dialog);
    }

}
