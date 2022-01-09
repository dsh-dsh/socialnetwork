package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RS.NotificationDataRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.model.entity.Notification;
import com.skillbox.socialnet.repository.NotificationRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PersonRepository personRepository;
    private final AuthService authService;
    private final NotificationRepository notificationRepository;


    public NotificationRS getNotification(int itemPerPage, int offset) {
        NotificationRS notificationRS = new NotificationRS();

        notificationRS.setError("error");
        notificationRS.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationRS.setOffset(offset);
        notificationRS.setPerPage(itemPerPage);
        notificationRS.setTotal(0);
        notificationRS.setData(createDataRS(notificationRepository.getFirstNotSeenNotificationsForUser(authService.getPersonFromSecurityContext().getId()).get()));

        return notificationRS;
    }

    public NotificationRS putNotification(boolean all, int id) {
        return new NotificationRS();
    }

    private NotificationDataRS createDataRS(Notification notification) {
        NotificationDataRS ndr = new NotificationDataRS();
        String[] whoIs = notification.getEntity().split(",");

        ndr.setEntityAuthor(personRepository.getPersonForNotification(Integer.parseInt(whoIs[0])));
        ndr.setEventType(notification.getType().toString());
        ndr.setId(notification.getId());
        ndr.setSentTime(notification.getSentTime());
        ndr.setInfo(notification.getEntity());
        return ndr;
    }

}
