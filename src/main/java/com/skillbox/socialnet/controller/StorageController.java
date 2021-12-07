package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StorageController {

    @PostMapping("/storage")
    public ResponseEntity<?> storage(@RequestParam("type") String type,
                                     @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(new DefaultRS<>());
    }

}
