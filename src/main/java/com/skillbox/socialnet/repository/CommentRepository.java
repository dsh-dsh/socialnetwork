package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    @Query(value = "select c from PostComment c where c.post = :post and c.isBlocked = false")
    List<PostComment> findByPost(Post post);

    @Query(value = "select c from PostComment c where c.post.id = :postId and c.isBlocked = false")
    List<PostComment> findByPostId(int postId, Pageable pageable);

    @Query(value = "select c from PostComment c where c.id = :id")
    Optional<PostComment> findById(int id);




}
