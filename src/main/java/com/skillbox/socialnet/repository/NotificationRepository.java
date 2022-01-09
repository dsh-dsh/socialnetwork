package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = "select * from notification where person_id = :id and seen = false limit 1",
    nativeQuery = true)
    Optional<Notification> getFirstNotSeenNotificationsForUser(int id);

    List<Notification> findAll();

    Optional<Notification> findNotificationById(int id);

    @Query(value = "select id from notification_type where code like :code",
    nativeQuery = true)
    int getIdOfCode(String code);
}
