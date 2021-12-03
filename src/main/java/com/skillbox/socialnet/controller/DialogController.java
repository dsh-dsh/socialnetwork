package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RS.DefaultRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
public class DialogController {

    @PostMapping
    public ResponseEntity<?> dialog() {
        return ResponseEntity.ok(new DefaultRS<>());
    }

}
