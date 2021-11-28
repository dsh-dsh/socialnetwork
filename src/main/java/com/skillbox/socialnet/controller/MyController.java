package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @PutMapping("accounts/notifications")
    public ResponseEntity<?> aaa(@RequestBody AccountNotificationRQ accountNotificationRQ) {
        System.out.println(accountNotificationRQ);
        return ResponseEntity.ok("");
    }

}
