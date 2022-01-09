package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.NotificationRS;
import com.skillbox.socialnet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/v1")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<NotificationRS> getNotification(
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") int itemPerPage,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {
        return ResponseEntity.ok(notificationService.getNotification(itemPerPage, offset));
    }

    @PutMapping("/notifications")
    public ResponseEntity<?> setNotification(
            @RequestParam(name = "all", required = false, defaultValue = "false") boolean all,
            @RequestParam(name = "id", required = false, defaultValue = "0") int id
    ) {
        return ResponseEntity.ok(notificationService.putNotification(all, id));
    }
}
