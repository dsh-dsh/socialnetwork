package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public ResponseEntity<String> email() {
        emailService.send("dan.shipilov@gmail.com", "subject", "text");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
