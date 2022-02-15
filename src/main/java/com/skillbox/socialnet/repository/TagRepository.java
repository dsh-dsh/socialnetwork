package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag getTagByTagName(String tag);

    Optional<Tag> findByTagName(String tag);
}
