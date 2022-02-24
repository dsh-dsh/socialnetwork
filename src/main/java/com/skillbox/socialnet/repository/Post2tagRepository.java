package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Post2tagRepository extends JpaRepository<Post2tag, Integer> {

    List<Post2tag> getAllByPost(Post post);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post2tag AS post2tag " +
            "WHERE post2tag.post = :post " +
            "AND post2tag.tag = :tag")
    void deleteByPostAndTag(Post post, Tag tag);

    @Transactional
    @Modifying
    @Query(value = "delete from post2tag as p where p.post_id = :id ",
            nativeQuery = true)
    void deleteForDeletedPost(int id);
}
