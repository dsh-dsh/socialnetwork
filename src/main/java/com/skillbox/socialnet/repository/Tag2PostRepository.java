package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Tag2PostRepository extends JpaRepository<Post2tag, Integer> {

    List<Post2tag> getAllByPost(Post post);

    @Transactional
    @Modifying
    @Query(value = "delete from post2tag as p where p.post_id = :id ",
            nativeQuery = true)
    void deleteForDeletedPost(int id);
}
