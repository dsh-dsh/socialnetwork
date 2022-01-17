package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.NotificationSetting;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SettingsRepository extends JpaRepository<NotificationSetting, Integer> {

    Optional<NotificationSetting> findByPersonAndNotificationTypeCode(Person person, NotificationTypeCode notificationTypeCode);

    List<NotificationSetting> findByPerson(Person person);

}
