package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RS.NotificationRS;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessageSendingOperations messagingTemplate;
    private static final String NOTIFICATION_TOPIC = "/ws/topic/notification/";

    public NotificationRS sendNotification(NotificationRS notificationRS, int receiverId) {
        String notificationTopic = NOTIFICATION_TOPIC + receiverId;
        messagingTemplate.convertAndSend(notificationTopic, notificationRS);
        return notificationRS;
    }

}
