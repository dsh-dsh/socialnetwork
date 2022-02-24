package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.rs.NotificationDataRS;
import com.skillbox.socialnet.model.rs.NotificationRS;
import com.skillbox.socialnet.model.dto.NotificationInterfaceProjectile;
import com.skillbox.socialnet.model.dto.CommentAuthorDTO;
import com.skillbox.socialnet.model.entity.Notification;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.repository.FriendshipRepository;
import com.skillbox.socialnet.repository.NotificationRepository;
import com.skillbox.socialnet.repository.PersonRepository;
import com.skillbox.socialnet.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;
    private final SettingsRepository settingsRepository;

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
        if (!all) {
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
        List<Integer> blockedUsers = friendshipRepository.getIdsIfBlocked(authService.getPersonFromSecurityContext().getId());
        if (isNotificationEnabled(dstPersonId, typeCode.toString()) && !blockedUsers.contains(dstPersonId)) {
            Timestamp sentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
            notificationRepository.createNewNotification(typeCode.ordinal(), sentTime, dstPersonId, String.valueOf(entityId), contact, false);
            sendNotifications(dstPersonId);
        }
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


    @Scheduled(cron = "0 * * * * *") //каждую минуту
    //@Scheduled(cron = "0 0 * * * *") //каждый час
    //@Scheduled(cron = "0 12,00 * * * *")//каждые 12 часов
    private void createBirthdayNotifications() {
        List<Integer> allIds = personRepository.getAllIds();
        for (Integer allId : allIds) {
            createBirthdayRS(allId);
        }
    }


    private void createBirthdayRS(int id) {
        List<NotificationInterfaceProjectile> ids = friendshipRepository.getIdsForNotification(id);

        for (NotificationInterfaceProjectile nip : ids) {

            if ((nip.getSrc() == id) && (personRepository.getIdIfBirthDayIsTomorrowOrToday(nip.getDst()) != null && isNotificationEnabled(id, NotificationTypeCode.FRIEND_BIRTHDAY.toString()))) {
                notificationRepository.createNewNotification(NotificationTypeCode.FRIEND_BIRTHDAY.ordinal(), new Timestamp(Calendar.getInstance().getTimeInMillis()), id, nip.getDst().toString(), personRepository.getEmailById(id), false);
            }


            if ((nip.getDst() == id) && (personRepository.getIdIfBirthDayIsTomorrowOrToday(nip.getSrc()) != null && isNotificationEnabled(id, NotificationTypeCode.FRIEND_BIRTHDAY.toString()))) {
                notificationRepository.createNewNotification(NotificationTypeCode.FRIEND_BIRTHDAY.ordinal(), new Timestamp(Calendar.getInstance().getTimeInMillis()), id, nip.getSrc().toString(), personRepository.getEmailById(id), false);
            }

        }
    }

    private boolean isNotificationEnabled(int id, String code) {
        return settingsRepository.getPermissionForPersonByType(id, code);
    }
}
