package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.model.dto.MessageSendDtoRequest;
import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final NotificationService notificationService;

    @MessageMapping("/ws/message/{receiverId}")
    public void getMessage(@DestinationVariable int receiverId, @Payload MessageSendDtoRequest message) {
        notificationService.createNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                receiverId,
                3,
                "p1@mail.ru");
    }

//    @SubscribeMapping("/ws/topic/notification/{receiverId}")
//    public NotificationRS notificationOnSubscribe(@DestinationVariable int receiverId) {
//        System.out.println("notificationOnSubscribe(" + receiverId + ")");
//        return notificationService.sendNotifications(receiverId);
//    }

    @GetMapping("/message/{receiverId}")
    public void checkNotification(@PathVariable int receiverId) {
        System.out.println("checkNotification()");
        notificationService.createNewNotification(
                NotificationTypeCode.FRIEND_REQUEST,
                receiverId,
                3,
                "p1@mail.ru");
        notificationService.sendNotifications(receiverId);
    }

}
