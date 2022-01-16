package com.skillbox.socialnet.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.RS.NotificationDataRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import com.skillbox.socialnet.model.entity.Notification;
import com.skillbox.socialnet.repository.NotificationRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PersonRepository personRepository;
    private final AuthService authService;
    private final NotificationRepository notificationRepository;

    @Value("${socialnet.item-per-page}")
    private int itemPerPage;

    @Value("${socialnet.offset}")
    private int offset;

    public NotificationRS getNotification(int itemPerPage, int offset) {
        List<Notification> notifications = notificationRepository.getAllNotSeenNotificationsForUser(authService.getPersonFromSecurityContext().getId());
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
        String[] whoIs = notification.getEntity().split(",");

        ndr.setEntityAuthor(CommentAuthorDTO.getCommentAuthorDTO(personRepository.findPersonById(Integer.parseInt(whoIs[0]))));
        ndr.setEventType(notification.getType().getName());
        ndr.setId(notification.getId());
        ndr.setSentTime(notification.getSentTime());
        ndr.setInfo(notification.getType().getName());
        return ndr;
    }

}
