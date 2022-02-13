package com.skillbox.socialnet.service;

import com.skillbox.socialnet.model.dto.MessageDTO;
import com.skillbox.socialnet.model.rs.NotificationRS;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessageSendingOperations messagingTemplate;
    private static final String NOTIFICATION_TOPIC = "/ws/topic/notification/";
    private static final String MESSAGE_TOPIC = "/ws/topic/message/";

    public void sendNotifications(NotificationRS notificationRS, int receiverId) {
        messagingTemplate.convertAndSend(NOTIFICATION_TOPIC + receiverId, notificationRS);
    }

    public void sendMessage(int receiverId, MessageDTO messageDTO) {
        messagingTemplate.convertAndSend(MESSAGE_TOPIC + receiverId, messageDTO);
    }
}
