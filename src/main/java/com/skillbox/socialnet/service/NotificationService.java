package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.rs.NotificationDataRS;
import com.skillbox.socialnet.model.rs.NotificationRS;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import com.skillbox.socialnet.model.entity.Notification;
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
import java.util.stream.Collectors;

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

    public void sendNotifications(int receiverId) {
        NotificationRS notificationRS = getNotificationRS(receiverId);
        webSocketService.sendNotifications(notificationRS, receiverId);
    }

    public void createNewNotification(NotificationTypeCode typeCode, int dstPersonId, int entityId, String contact) {
        Timestamp sentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
        notificationRepository.createNewNotification(typeCode.ordinal(), sentTime, dstPersonId, String.valueOf(entityId), contact, false);
        sendNotifications(dstPersonId);
    }

    public NotificationRS getNotificationRS(int receiverId) {
        NotificationRS notificationRS = new NotificationRS();
        notificationRS.setData(getNotificationDataRSList(receiverId));
        notificationRS.setError("string");
        notificationRS.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationRS.setOffset(offset);
        notificationRS.setPerPage(itemPerPage);
        notificationRS.setTotal(0);

        return notificationRS;
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

    private List<NotificationDataRS> getNotificationDataRSList(int receiverId) {
        return notificationRepository
                .getAllNotSeenNotificationsForUser(receiverId)
                .stream()
                .map(this::createDataRS)
                .collect(Collectors.toList());
    }
}
