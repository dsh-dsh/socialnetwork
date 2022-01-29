package com.skillbox.socialnet.service;

import com.skillbox.socialnet.exception.BadRequestException;
import com.skillbox.socialnet.model.RS.NotificationDataRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import com.skillbox.socialnet.model.entity.Notification;
import com.skillbox.socialnet.model.entity.NotificationType;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.repository.NotificationRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PersonRepository personRepository;
    private final AuthService authService;
    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;

    @Value("${socialnet.item-per-page}")
    private int itemPerPage = 20;

    @Value("${socialnet.offset}")
    private int offset = 0;

    public NotificationRS getNotification(int itemPerPage, int offset) {
        List<Notification> notifications = notificationRepository
                .getAllNotSeenNotificationsForUser(authService.getPersonFromSecurityContext().getId());
        NotificationRS notificationRS = new NotificationRS();
        List<NotificationDataRS> dataRS = new ArrayList<>();
        for (Notification notification : notifications) {
            dataRS.add(createDataRS(notification));
        }
        notificationRS.setData(dataRS);
        notificationRS.setError("string");
        notificationRS.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationRS.setOffset(offset);
        notificationRS.setPerPage(itemPerPage);
        notificationRS.setTotal(0);

        return notificationRS;
    }

    public NotificationRS setNotification(boolean all, int id) {
        int currentPersonID = authService.getPersonFromSecurityContext().getId();
        if(!all){
            notificationRepository.makeOneNotificatonRead(id);
        } else {
            notificationRepository.makeAllNotificationRead(currentPersonID);
        }

        return getNotification(itemPerPage, offset);
    }

    private NotificationDataRS createDataRS(Notification notification) {
        NotificationDataRS ndr = new NotificationDataRS();
        ndr.setEntityAuthor(CommentAuthorDTO.getCommentAuthorDTO(personRepository.findPersonById(Integer.parseInt(notification.getEntity()))));
        ndr.setEventType(notification.getType().getName());
        ndr.setId(notification.getId());
        ndr.setSentTime(notification.getSentTime());
        ndr.setInfo(notification.getType().getName());

        return ndr;
    }

    private NotificationRS createNotificationRS(List<NotificationDataRS> dataRSList) {
        NotificationRS notificationRS = new NotificationRS();
        notificationRS.setData(dataRSList);
        notificationRS.setError("string");
        notificationRS.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationRS.setOffset(offset);
        notificationRS.setPerPage(itemPerPage);
        notificationRS.setTotal(0);

        return notificationRS;
    }

    public void createAndSendNewNotification(NotificationTypeCode typeCode, int dstPersonId, int entityId, String contact) {
        Notification notification = createAndGetNotification(typeCode, dstPersonId, entityId, contact);
        System.out.println(notification.getId());
        sendNotification(notification);
    }

    private Notification createAndGetNotification(NotificationTypeCode typeCode, int dstPersonId, int entityId, String contact) {
        Timestamp sentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
        notificationRepository.createNewNotification(typeCode.ordinal(), sentTime, dstPersonId, String.valueOf(entityId), contact, false);
        return notificationRepository
                .findBySentTimeAndEntityAndContactAndSeen(sentTime, String.valueOf(entityId), contact, false)
                .orElseThrow(BadRequestException::new);
    }

    private void sendNotification(Notification notification) {
        NotificationDataRS notificationDataRS = createDataRS(notification);
        NotificationRS notificationRS = createNotificationRS(List.of(notificationDataRS));
        webSocketService.sendNotification(notificationRS);
    }
}
