package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT DISTINCT post " +
            "FROM Post AS post " +
            "JOIN post.tags AS tags " +
            "WHERE (:authorName is null OR post.author.firstName LIKE %:authorName% OR post.author.lastName LIKE %:authorName%) " +
            "AND (:text is null OR post.postText LIKE %:text% OR post.title LIKE %:text%) " +
            "AND ((:tags) is null OR tags.tag.tag IN (:tags)) " +
            "AND post.time BETWEEN :timeFrom AND :timeTo")
    Page<Post> findPost(String authorName, String text, Timestamp timeFrom, Timestamp timeTo, List<String> tags, Pageable pageable);

    Optional<Post> findPostById(int id);

    Page<Post> findByAuthorIn(List<Person> persons, Pageable pageable);

    Optional<List<Post>> findOptionalByAuthorIn(List<Person> friends);

    @Query("FROM Post")
    Optional<Page<Post>> getOptionalPageAll(Pageable pageable);

    Page<Post> findPostsByAuthor(Person author, Pageable pageable);

    Post getPostByAuthor(Person person);

    @Query("SELECT post " +
            "FROM Post AS post " +
            //"JOIN post.author AS author " +
            "WHERE post NOT IN (:posts) " +
            "ORDER BY post.author.regDate DESC")
    List<Post> findOrderByNewAuthorsExclude(List<Post> posts, Pageable pageable);

}
