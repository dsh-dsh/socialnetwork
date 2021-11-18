package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.NotificationRQ;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1/notifications")
public class NotificationController {
        private final NotificationService notificationService;

        @Autowired
        public NotificationController(NotificationService notificationService){
                this.notificationService = notificationService;
        }

        @GetMapping("")
        public ResponseEntity<NotificationRS> getNotification(NotificationRQ notificationRQ){
                return ResponseEntity.ok(notificationService.getNotification(notificationRQ));
        }
}
