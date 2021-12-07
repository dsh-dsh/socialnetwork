package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StorageController {

    private final StorageService storageService;
    @PostMapping("/storage")
    public ResponseEntity<?> storage(@RequestParam("type") String type,
                                     @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(storageService.saveImageToProfile(type, file));
    }

}
