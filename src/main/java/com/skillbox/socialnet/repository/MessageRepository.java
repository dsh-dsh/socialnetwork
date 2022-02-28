package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Message;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.MessageReadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Message AS messages WHERE messages IN (:messages)")
    void deleteMessagesByList(List<Message> messages);

    Page<Message> findByDialog(Dialog dialog, Pageable pageable);

    List<Message> findByDialog(Dialog dialog);

    Optional<Message> findFirst1ByDialogOrderByTimeDesc(Dialog dialog);

    int countByDialogAndRecipientAndReadStatus(Dialog dialog, Person recipient, MessageReadStatus readStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Message AS message " +
            "SET message.readStatus = :status " +
            "WHERE message IN (:messages) " +
            "AND message.recipient = :recipient")
    void setMessagesReadStatus(List<Message> messages, Person recipient, MessageReadStatus status);

    int countByDialog(Dialog dialog);
}
