package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RQ.DialogCreateDTORequest;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralListResponse;
import com.skillbox.socialnet.model.dto.*;
import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.mapper.DefaultRSMapper;
import com.skillbox.socialnet.repository.DialogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        Person me = authService.getPersonFromSecurityContext();
        Page<Dialog> dialogPage = dialogRepository.findByPerson(me, pageable);
        List<DialogDTO> dialogDTOS = dialogPage.getContent()
                .stream()
                .map(dialog -> new DialogDTO(
                        dialog, me,
                        messageService.getLastMessage(dialog),
                        messageService.getUnreadCount(dialog)))
                .collect(Collectors.toList());
        return new GeneralListResponse<>(dialogDTOS, dialogPage);
    }

    public DefaultRS<?> createDialog(DialogCreateDTORequest dialogCreateDTORequest) {
        Person me = authService.getPersonFromSecurityContext();
        Set<Person> persons = personService.getPersonsByIdList(dialogCreateDTORequest.getUserIds());
        persons.add(me);
        Dialog dialog = getDialogByPersonSet(me, persons)
                .orElseGet(() -> addNewDialogToDataBase(persons));
        return DefaultRSMapper.of(new DialogIdDTO(dialog.getId()));
    }

    public DefaultRS<?> deleteDialog(long id) {
        Dialog dialog = dialogRepository.getOne(id);
        List<Message> messages = dialog.getMessages();
        messageService.deleteMessages(messages);
        dialogRepository.delete(dialog);
        return DefaultRSMapper.of(new DialogIdDTO(dialog.getId()));
    }

    public DefaultRS<?> getMessagesInDialog(long dialogId, Pageable pageable) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Page<Message> messagePage = messageService.getMessagesByDialog(dialog, pageable);
        List<Message> messages = messagePage.getContent();
        return DefaultRSMapper.of(messages, messagePage);
    }

    public DefaultRS<?> addUsersToDialog(long dialogId, DialogCreateDTORequest dialogCreateDTORequest) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Set<Person> persons = personService.getPersonsByIdList(dialogCreateDTORequest.getUserIds());
        dialog.getPersons().addAll(persons);
        dialogRepository.save(dialog);
        List<Integer> idList = persons.stream().map(Person::getId).collect(Collectors.toList());
        return DefaultRSMapper.of(new DialogCreateDTORequest(idList));
    }

    public DefaultRS<?> deleteUsersFromDialog(long dialogId, String ids) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        List<Integer> personIdList = new ArrayList<>(); // TODO выяснить формат String ids
        Set<Person> persons = personService.getPersonsByIdList(personIdList);
        dialog.getPersons().removeAll(persons);
        dialogRepository.save(dialog);
        List<Integer> idList = persons.stream().map(Person::getId).collect(Collectors.toList());
        return DefaultRSMapper.of(new DialogCreateDTORequest(idList));
    }

    public DefaultRS<?> unread() {
        Person me = authService.getPersonFromSecurityContext();
        Set<Dialog> dialogs = me.getDialogs();
        int unreadCount = (int) messageService.countUnreadMessages(dialogs);
        return DefaultRSMapper.of(new UnreadCountDTO(unreadCount));
    }

    public DefaultRS<?> getLinkToJoin(long dialogId) {
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        String link = ""; // TODO выяснить формат String link
        return DefaultRSMapper.of(new InviteLinkDTO(link));
    }

    public DefaultRS<?> joinByLink(long dialogId) {
        Person person = authService.getPersonFromSecurityContext();
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        Set<Person> personSet = dialog.getPersons();
        personSet.add(person);
        dialogRepository.save(dialog);
        List<Integer> idList = personSet.stream()
                .map(Person::getId)
                .collect(Collectors.toList());
        return DefaultRSMapper.of(new DialogCreateDTORequest(idList));
    }

    public MessageDTO sendMessage(long dialogId, MessageResponseDTO messageResponseDTO) {

        Person author = authService.getPersonFromSecurityContext();
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(BadRequestException::new);
        String text = messageResponseDTO.getMessageText();
        Message message = messageService.addMessage(dialog, author, text);
        dialog.getMessages().add(message);
        dialogRepository.save(dialog);

        return new MessageDTO(message); // new GeneralListResponse<>(new MessageDTO(message));
    }

    private Optional<Dialog> getDialogByPersonSet(Person me, Set<Person> persons) {
//        System.out.println("getDialogByPersonSet()");
        List<Dialog> dialogs = dialogRepository.findByPerson(me);
        return dialogs.stream()
//                .peek(d -> soutDialog(d, persons))
                .filter(dialog -> dialog.getPersons().containsAll(persons))
                .findFirst();
    }

    void soutDialog(Dialog dialog, Set<Person> persons) {
        System.out.println("dialogId = " + dialog.getId());
        dialog.getPersons().forEach(p -> System.out.print(p.getFirstName() + " " + p.getLastName() + " "));
        System.out.println();
        persons.forEach(p -> System.out.print(p.getFirstName() + " " + p.getLastName() + " "));
        System.out.println();
        System.out.println("---------------------");
    }

    private Dialog addNewDialogToDataBase(Set<Person> persons) {
//        System.out.println("addNewDialogToDataBase()");
        Dialog dialog = new Dialog(persons);
        return dialogRepository.save(dialog);
    }

//    public Person getRecipient(Dialog dialog, Person author) {
//        return dialog.getPersons().stream()
//                .filter(person -> !person.equals(author))
//                .findFirst()
//                .orElseThrow(BadRequestException::new);
//    }
}
