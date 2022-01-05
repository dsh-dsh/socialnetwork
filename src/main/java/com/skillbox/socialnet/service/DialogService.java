package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.DialogRepository;
import com.skillbox.socialnet.util.anotation.ПокаНеИспользуется;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class DialogService {

    private final DialogRepository dialogRepository;
    private final MessageService messageService;
    private final PersonService personService;
    private final AuthService authService;

    public GeneralListResponse<?> getDialogs(Pageable pageable) {
        Person author = authService.getPersonFromSecurityContext();
        Page<Dialog> dialogPage = dialogRepository.findByPerson(author, pageable);
        List<DialogDTO> dialogDTOS = getDialogDTOList(author, dialogPage.getContent());
        return new GeneralListResponse<>(dialogDTOS, dialogPage);
    }

    public List<Dialog> getDialogs(Person author) {
        return dialogRepository.findByPerson(author);
    }

    public DialogIdDTO createDialog(DialogCreateDTORequest dialogCreateDTORequest) {
        Set<Person> persons = personService.getPersonsByIdList(dialogCreateDTORequest.getUserIds());
        Person author = authService.getPersonFromSecurityContext();
        persons.add(author);
        Dialog dialog = getDialogByPersonSet(author, persons)
                .orElseGet(() -> addNewDialogToDataBase(persons));
        return new DialogIdDTO(dialog.getId());
    }

    public GeneralListResponse<?> getMessagesInDialog(long dialogId, Pageable pageable) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Person author = authService.getPersonFromSecurityContext();
        Person recipient = getRecipient(dialog, author);
        Page<Message> messagePage = messageService.getMessagePageByDialog(dialog, pageable);
        messageService.setMessagesStatusRead(messagePage.getContent(), recipient);
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
        dialog.getMessages().add(message);
        dialogRepository.save(dialog);
        return new MessageDTO(author, message);
    }

    private List<DialogDTO> getDialogDTOList(Person author, Collection<Dialog> dialogs) {
        return dialogs.stream()
                .map(dialog -> new DialogDTO(
                        dialog, author,
                        messageService.getLastMessage(dialog),
                        messageService.getUnreadCount(dialog, author)))
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

    @ПокаНеИспользуется
    public MessageResponseDTO readMessage(long dialogId, int messageId) {
        Message message = messageService.getMessageToRead(messageId);
        return new MessageResponseDTO(message.getMessageText());
    }

    @ПокаНеИспользуется
    public DialogIdDTO deleteDialog(long id) {
        Dialog dialog = dialogRepository.getOne(id);
        List<Message> messages = messageService.getMessagesByDialog(dialog);
        messageService.deleteMessages(messages);
        dialogRepository.delete(dialog);
        return new DialogIdDTO(dialog.getId());
    }

    @ПокаНеИспользуется
    public DialogCreateDTORequest addUsersToDialog(long dialogId, DialogCreateDTORequest dialogCreateDTORequest) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Set<Person> persons = personService.getPersonsByIdList(dialogCreateDTORequest.getUserIds());
        dialog.getPersons().addAll(persons);
        dialogRepository.save(dialog);
        List<Integer> idList = persons.stream().map(Person::getId).collect(Collectors.toList());
        return new DialogCreateDTORequest(idList);
    }

    @ПокаНеИспользуется
    public DialogCreateDTORequest deleteUsersFromDialog(long dialogId, String ids) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        List<Integer> personIdList = new ArrayList<>(); // TODO выяснить формат String ids
        Set<Person> persons = personService.getPersonsByIdList(personIdList);
        dialog.getPersons().removeAll(persons);
        dialogRepository.save(dialog);
        List<Integer> idList = persons.stream().map(Person::getId).collect(Collectors.toList());
        return new DialogCreateDTORequest(idList);
    }

    @ПокаНеИспользуется
    public InviteLinkDTO getLinkToJoin(long dialogId) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        String link = ""; // TODO выяснить формат String link
        return new InviteLinkDTO(link);
    }

    @ПокаНеИспользуется
    public DialogCreateDTORequest joinByLink(long dialogId) {
        Person person = authService.getPersonFromSecurityContext();
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Set<Person> personSet = dialog.getPersons();
        personSet.add(person);
        dialogRepository.save(dialog);
        List<Integer> idList = personSet.stream()
                .map(Person::getId)
                .collect(Collectors.toList());
        return new DialogCreateDTORequest(idList);
    }
}
