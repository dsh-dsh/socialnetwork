package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.NotificationRQ;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/v1/")
public class NotificationController {
        private final NotificationService notificationService;

        @GetMapping("notifications")
        public ResponseEntity<NotificationRS> getNotification(NotificationRQ notificationRQ){
                return ResponseEntity.ok(notificationService.getNotification(notificationRQ));
        }
}
