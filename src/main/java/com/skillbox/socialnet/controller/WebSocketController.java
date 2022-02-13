package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rs.NotificationRS;
import com.skillbox.socialnet.model.dto.MessageSendDtoRequest;
import com.skillbox.socialnet.service.DialogService;
import com.skillbox.socialnet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final NotificationService notificationService;
    private final DialogService dialogService;

    @MessageMapping("/message/{dialogId}/{authorId}")
    public void getMessage(
            @DestinationVariable long dialogId,
            @DestinationVariable int authorId,
            @Payload MessageSendDtoRequest message) {
        dialogService.sendMessageByWebSocket(dialogId, authorId, message);
    }

    @SubscribeMapping("/notification/{receiverId}")
    public NotificationRS notificationOnSubscribe(@DestinationVariable int receiverId) {
        return notificationService.getNotificationRS(receiverId);
    }

}
