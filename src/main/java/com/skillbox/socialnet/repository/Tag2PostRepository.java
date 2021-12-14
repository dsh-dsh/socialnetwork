package com.skillbox.socialnet.repository;


import com.skillbox.socialnet.model.entity.Post2tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Tag2PostRepository extends JpaRepository<Post2tag, Integer> {
}
