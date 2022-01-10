package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<PostLike, Integer> {

    List<PostLike> findAllByPost(Post post);

    Optional<PostLike> findByPostAndPerson(Post post, Person person);


}
