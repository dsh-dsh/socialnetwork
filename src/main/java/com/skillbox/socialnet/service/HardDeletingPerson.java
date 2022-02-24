package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HardDeletingPerson {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final Post2tagRepository post2tagRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final FriendshipRepository friendshipRepository;

    @Scheduled(cron = "${log.files.scheduling.cron.expression}")
    public void deletePerson() {
        Timestamp timestamp = Timestamp.valueOf(LocalDate.now().minusMonths(1).atStartOfDay());
        List<Person> deletedPersons = personRepository.findAllByDeleted(true, timestamp);
        for (Person p :
                deletedPersons) {
            friendshipRepository.deleteForDeletedPerson(p.getId());
            notificationRepository.deleteForDeletedPerson(p.getId());
            likesRepository.deleteForDeletedPerson(p.getId());
            List<Post> posts = postRepository.findAllByAuthor(p);
            for (Post postForDelete :
                    posts) {
                likesRepository.deleteForDeletedPost(postForDelete.getId());
                post2tagRepository.deleteForDeletedPost(postForDelete.getId());
                commentRepository.deleteForDeletedPost(postForDelete.getId());
            }
            postRepository.deleteForDeletedPerson(p.getId());
            commentRepository.deleteForDeletedPerson(p.getId());
            if (p.getPhoto() != null) {
                Cloud.deletePhotoFromCloud(p.getPhoto());
            }
        }
        personRepository.deleteAll(deletedPersons);
    }
}
