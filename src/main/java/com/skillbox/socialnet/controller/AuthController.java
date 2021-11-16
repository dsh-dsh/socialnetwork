package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login
            (@RequestBody AuthUserRQ authUserRQ)
    {
        System.out.println("jbhj");
        return ResponseEntity.ok(authService.login());
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout () {
        return ResponseEntity.ok(authService.logout());
    }

}
