package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.AccountNotificationRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.FileDTO;
import com.skillbox.socialnet.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StorageController {

    private final StorageService storageService;
    @PostMapping("/storage")
    public ResponseEntity<GeneralResponse<FileDTO>> storage(@RequestParam("type") String type,
                                                            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(new GeneralResponse<>(storageService.saveImageToProfile(type, file)));
    }

}
