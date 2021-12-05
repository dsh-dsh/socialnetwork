package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query(value = "select p from Post p where (p.postText like %:text% or p.title like %:text%) and p.time between :postTime and :stopTime and p.isBlocked = false order by p.time desc" )
    Page<Post> findPostByPostTextAndTimeBetween(Timestamp postTime, Timestamp stopTime, String text, Pageable pageable);

    @Query(value = "select p from Post p where (p.postText like %:text% or p.title like %:text%) and p.isBlocked = false order by p.time desc" )
    Page<Post> findPostByPostText(String text, Pageable pageable);

    Optional<Post> findPostById(int id);

    Page<Post> findPostsByAuthor(Person author, Pageable pageable);

//    @Query(value = "select p from Post p where p.postText LIKE %:queryText%" )
//    Page<Post> findPostByPostText(Pageable pageable, String queryText);

}
