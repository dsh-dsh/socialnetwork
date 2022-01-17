package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    List<PostComment> findByPostAndIsBlocked(Post post, boolean isBlocked);

    Page<PostComment> findByPostAndIsBlocked(Post post, boolean isBlocked, Pageable pageable);

    Optional<PostComment> findById(int id);

}
