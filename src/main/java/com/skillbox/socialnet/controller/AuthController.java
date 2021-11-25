package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Log
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login
            (@RequestBody AuthUserRQ authUserRQ) {
        DefaultRS defaultRS = authService.login(authUserRQ);
        if (defaultRS.getError().equals("string")){
            return ResponseEntity.ok(defaultRS);
        }
        return new ResponseEntity<>(defaultRS, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout () {
        return ResponseEntity.ok(authService.logout());
    }

}
