package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rs.NotificationRS;
import com.skillbox.socialnet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final NotificationService notificationService;

    @SubscribeMapping("/notification/{receiverId}")
    public NotificationRS notificationOnSubscribe(
            @DestinationVariable int receiverId) {
        return notificationService.getNotificationRS(receiverId);
    }

}
