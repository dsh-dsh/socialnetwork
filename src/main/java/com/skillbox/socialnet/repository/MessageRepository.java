package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("DELETE FROM Message AS messages WHERE messages IN (:messages)")
    void deleteMessagesByList(List<Message> messages);

    Page<Message> findByDialog(Dialog dialog);
}
