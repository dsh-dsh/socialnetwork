package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    @Query(value = "update notification set seen = true where person_id = :id",
    nativeQuery = true)
    void makeAllNotificationRead(int id);

    @Transactional
    @Modifying
    @Query(value = "insert into notification(type_id, sent_time, person_id, entity_id, contact, seen)" +
                   "values(:typeId, :sentTime, :personId, :entityId, :contact, :seen)",
    nativeQuery = true)
    void createNewNotification(int typeId, Timestamp sentTime, int personId, String entityId, String contact, boolean seen);

    Optional<Notification> findBySentTimeAndEntityAndContactAndSeen(Timestamp sentTime, String entity, String contact, boolean seen);


    @Transactional
    @Modifying
    @Query(value = "delete from notification as n where n.person_id = :id",
            nativeQuery = true)
    void deleteForDeletedPerson(int id);
}
