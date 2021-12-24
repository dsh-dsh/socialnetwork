package com.skillbox.socialnet.repository;


import com.skillbox.socialnet.model.entity.Post;
import com.skillbox.socialnet.model.entity.Post2tag;
import com.skillbox.socialnet.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Tag2PostRepository extends JpaRepository<Post2tag, Integer> {
    List<Post2tag> getAllByPost(Post post);

    List<Tag> getAllById(int id);
}
