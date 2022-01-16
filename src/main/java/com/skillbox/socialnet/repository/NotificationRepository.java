package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = "select * from notification where person_id = :id and seen = false",
    nativeQuery = true)
    List<Notification> getAllNotSeenNotificationsForUser(int id);

    List<Notification> findAll();

    Optional<Notification> findNotificationById(int id);

    @Query(value = "select id from notification_type where code like :code",
    nativeQuery = true)
    int getIdOfCode(String code);

    @Transactional
    @Modifying
    @Query(value = "update notification set seen = true where id = :id",
    nativeQuery = true)
    void makeOneNotificatonRead(int id);

    @Transactional
    @Modifying
    @Query(value = "update notification set seen = true where entity_id = :id",
    nativeQuery = true)
    void makeAllNotificationRead(int id);
}
