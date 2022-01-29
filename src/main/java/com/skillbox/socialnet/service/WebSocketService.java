package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.RS.NotificationRS;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final AuthService authService;
    private final SimpMessageSendingOperations messagingTemplate;

    int currentPersonId = authService.getPersonFromSecurityContext().getId();
    private final String notificationTopic = "/topic/notification/" + currentPersonId;

    public NotificationRS sendNotification(NotificationRS notificationRS) {
        messagingTemplate.convertAndSend(notificationTopic, notificationRS);
        return notificationRS;
    }

}
