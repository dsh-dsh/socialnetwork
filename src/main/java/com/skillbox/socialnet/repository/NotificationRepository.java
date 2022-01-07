package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findAll();

    Optional<Notification> findNotificationById(int id);
}
