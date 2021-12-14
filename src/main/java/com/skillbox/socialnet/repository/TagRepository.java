package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag getTagByTag(String tag);
}
