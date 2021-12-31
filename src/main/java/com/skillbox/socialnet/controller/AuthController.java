package com.skillbox.socialnet.controller;


import com.skillbox.socialnet.model.RQ.AuthUserRQ;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserRQ authUserRQ) {
        GeneralResponse<UserDTO> response =
                new GeneralResponse<>(authService.login(authUserRQ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout () {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(authService.logout());
        return ResponseEntity.ok(response);
    }

}
