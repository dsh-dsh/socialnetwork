package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.MessageSendDtoRequest;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.service.NotificationService;
import com.skillbox.socialnet.util.anotation.MethodLog;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final NotificationService notificationService;

    @MethodLog
    @MessageMapping("/ws/message/{receiverId}")
    public void getMessage(@DestinationVariable int receiverId, @Payload MessageSendDtoRequest message) {
        System.out.println("WebSocketController getMessage()");
        System.out.println(message);

        notificationService.createAndSendNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                receiverId,
                3,
                "p1@mail.ru");
    }

    @GetMapping("/message/{receiverId}")
    public void checkNotification(@PathVariable int receiverId) {
        System.out.println("WebSocketController checkNotification()");

        notificationService.createAndSendNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                receiverId,
                3,
                "p1@mail.ru");
    }

}
