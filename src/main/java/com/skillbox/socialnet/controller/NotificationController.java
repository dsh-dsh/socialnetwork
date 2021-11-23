package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.NotificationRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1/notifications")
public class NotificationController {


        private final NotificationService notificationService;

        public NotificationController(NotificationService notificationService) {
                this.notificationService = notificationService;
        }


//        @GetMapping("/notifications")
//        @GetMapping
//        public ResponseEntity<?> getNotifications(
//                @RequestParam (defaultValue = "0") int offset,
//                @RequestParam(defaultValue = "20") int itemPerPage
//        ){
//                NotificationRQ notificationRQ = new NotificationRQ(offset, itemPerPage);
//                return ResponseEntity.ok(notificationService.getNotification(notificationRQ));
//        }

//        @GetMapping
//        public ResponseEntity<NotificationRS> getNotification(
//                @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
//                @RequestParam (name = "itemPerPage", required = false, defaultValue = "20") int itemPerPage)
//        {
//                NotificationRQ notificationRQ = new NotificationRQ(offset, itemPerPage);
//                return ResponseEntity.ok(notificationService.getNotification(notificationRQ));
//        }

}
