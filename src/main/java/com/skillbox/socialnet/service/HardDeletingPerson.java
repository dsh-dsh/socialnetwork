package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import com.skillbox.socialnet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HardDeletingPerson {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final FriendshipRepository friendshipRepository;

    //    @Scheduled(cron = "${log.files.scheduling.cron.expression}")
    @Scheduled(fixedRate = 5000L)
    public void deletePerson() {
        List<Person> deletedPersons = personRepository.findAllByDeleted(true);
        for (Person p :
                deletedPersons) {
            friendshipRepository.deleteForDeletedPerson(p.getId());
            notificationRepository.deleteForDeletedPerson(p.getId());
            likesRepository.deleteForDeletedPerson(p.getId());
            List<Post> posts = postRepository.findAllByAuthor(p);
            for (Post postForDelete :
                    posts) {
                likesRepository.deleteForDeletedPost(postForDelete.getId());
                tag2PostRepository.deleteForDeletedPost(postForDelete.getId());
                commentRepository.deleteForDeletedPost(postForDelete.getId());
            }
            postRepository.deleteForDeletedPerson(p.getId());
            commentRepository.deleteForDeletedPerson(p.getId());
            Cloud.deletePhotoFromCloud(p.getPhoto());
        }
        personRepository.deleteAll(deletedPersons);
        System.out.println("Scheduled successful");
    }
}
