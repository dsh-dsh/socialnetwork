package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("DELETE FROM Message AS messages WHERE messages IN (:messages)")
    void deleteMessagesByList(List<Message> messages);

    Page<Message> findByDialog(Dialog dialog, Pageable pageable);

    Optional<Message> findFirst1ByDialogOrderByTimeDesc(Dialog dialog);

    int countByDialogAndReadStatus(Dialog dialog, MessageReadStatus readStatus);
}
