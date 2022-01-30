package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@RequiredArgsConstructor
public class hardDeletingPerson {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
//    @Scheduled(cron = "${log.files.scheduling.cron.expression}")
    @Scheduled(fixedRate = 1000L)
    public void deletePerson(){
        List<Person> deletedPersons = personRepository.findAllByDeleted(true);
        personRepository.deleteAll(deletedPersons);
    }
}
