package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserRQ authUserRQ) {
        DefaultRS<UserDTO> defaultRS = authService.login(authUserRQ);
        return ResponseEntity.ok(defaultRS);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout () {
        return ResponseEntity.ok(authService.logout());
    }

}
