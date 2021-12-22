package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT post FROM Post AS post " +
            "WHERE (:text is null OR post.postText LIKE %:text% OR post.title LIKE %:text%) " +
            "AND post.time BETWEEN :timeFrom AND :timeTo")
    Page<Post> findPostBySearchRequest(String text, Timestamp timeFrom, Timestamp timeTo, Pageable pageable);

    Optional<Post> findPostById(int id);

    Optional<List<Post>> findByAuthorIn(List<Person> persons);

    @Query("FROM Post")
    Optional<Page<Post>> getOptionalPageAll(Pageable pageable);

    Page<Post> findPostsByAuthor(Person author, Pageable pageable);
    Post getPostByAuthor(Person person);

//    @Query(value = "select p from Post p where p.postText LIKE %:queryText%" )
//    Page<Post> findPostByPostText(Pageable pageable, String queryText);

}
