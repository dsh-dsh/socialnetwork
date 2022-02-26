package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT DISTINCT post " +
            "FROM Post AS post " +
            "JOIN post.tags AS tags " +
            "WHERE (:authorName is null OR post.author.firstName LIKE %:authorName% OR post.author.lastName LIKE %:authorName%) " +
            "AND (:text is null OR post.postText LIKE %:text% OR post.title LIKE %:text%) " +
            "AND post.time BETWEEN :timeFrom AND :timeTo")
    Page<Post> findPost(String authorName, String text, Timestamp timeFrom, Timestamp timeTo, Pageable pageable);

    @Query("SELECT DISTINCT post " +
            "FROM Post AS post " +
            "JOIN post.tags AS tags " +
            "WHERE (:authorName is null OR post.author.firstName LIKE %:authorName% OR post.author.lastName LIKE %:authorName%) " +
            "AND (:text is null OR post.postText LIKE %:text% OR post.title LIKE %:text%) " +
            "AND tags.tag.tagName IN (:tags) " +
            "AND post.time BETWEEN :timeFrom AND :timeTo")
    Page<Post> findPostWithTags(String authorName, String text, Timestamp timeFrom, Timestamp timeTo, List<String> tags, Pageable pageable);

    Optional<Post> findPostById(int id);

    Post findById(int id);

    Page<Post> findByAuthorIn(List<Person> persons, Pageable pageable);

    Optional<List<Post>> findOptionalByAuthorIn(List<Person> friends);

    @Query("FROM Post")
    Optional<Page<Post>> getOptionalPageAll(Pageable pageable);

    Page<Post> findPostsByAuthor(Person author, Pageable pageable);

    @Query("SELECT post " +
            "FROM Post AS post " +
            "WHERE post.author NOT IN (:persons) " +
            "ORDER BY post.author.regDate DESC")
    List<Post> findOrderByNewAuthorsExclude(List<Person> persons, Pageable pageable);

    @Query(value = "select distinct(author_id) from post_comment " +
            " where post_id = :postId and author_id != :authorId",
    nativeQuery = true)
    List<Integer> getIdsForPostNotifications(int postId, int authorId);

    List<Post> findAllByAuthor(Person person);

    @Transactional
    @Modifying
    @Query(value = "delete from post as p where p.author_id = :id ",
            nativeQuery = true)
    void deleteForDeletedPerson(int id);

    @Query(value = "select count(*) from post where author_id = :id",
            nativeQuery = true)
    Integer getSumOfPostsById(int id);

    @Query(value = "select count(*) from post_like where person_id = :id",
            nativeQuery = true)
    Integer getSumOfLikes(int id);

    @Query(value = "select MIN(time) from post where author_id = :id",
            nativeQuery = true)
    Timestamp getFirstPublication(int id);

    @Query(value = "select count(*) from post_comment where author_id = :id",
            nativeQuery = true)
    Integer getSumOfComments(int id);

    Page<Post> findByAuthorAndTimeBefore(Person author, Timestamp time, Pageable pageable);

    Page<Post> findByAuthorAndTimeAfter(Person author, Timestamp time, Pageable pageable);
}
