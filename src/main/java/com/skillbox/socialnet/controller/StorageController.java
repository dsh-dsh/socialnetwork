package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.dto.FileDTO;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.service.AuthService;
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
    private final AuthService  authService;
    
    @PostMapping("/storage")
    public ResponseEntity<GeneralResponse<FileDTO>> storage(
            @RequestParam("type") String type,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                new GeneralResponse<>(storageService.saveImageToProfile(type, file)));
    }

}
