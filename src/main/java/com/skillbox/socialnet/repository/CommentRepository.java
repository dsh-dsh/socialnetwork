package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    List<PostComment> findByPostAndParentAndIsBlocked(Post post, PostComment parent, boolean isBlocked);

    Page<PostComment> findByPostAndIsBlocked(Post post, boolean isBlocked, Pageable pageable);

    Optional<PostComment> findById(int id);

    List<PostComment> findByParent(PostComment parent);

    @Transactional
    @Modifying
    @Query(value = "delete from post_comment as p where p.post_id = :id",
            nativeQuery = true)
    void deleteForDeletedPost(int id);
    @Transactional
    @Modifying
    @Query(value = "delete from post_comment as p where p.author_id = :id",
            nativeQuery = true)
    void deleteForDeletedPerson(int id);
}
