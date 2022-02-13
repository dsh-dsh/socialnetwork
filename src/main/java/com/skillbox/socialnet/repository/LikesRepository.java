package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<PostLike, Integer> {

    List<PostLike> findAllByPost(Post post);

    Optional<PostLike> findByPostAndPerson(Post post, Person person);

    @Transactional
    @Modifying
    @Query(value = "delete from post_like as p where p.person_id = :id ",
            nativeQuery = true)
    void deleteForDeletedPerson(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from post_like as p where p.post_id = :id ",
            nativeQuery = true)
    void deleteForDeletedPost(int id);
}
