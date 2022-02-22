package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rq.AuthUserRQ;
import com.skillbox.socialnet.model.rs.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.model.dto.UserDTO;
import com.skillbox.socialnet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<UserDTO>> login(@RequestBody AuthUserRQ authUserRQ) {
        GeneralResponse<UserDTO> response =
                new GeneralResponse<>(authService.login(authUserRQ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> logout () {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(authService.logout());

        return ResponseEntity.ok(response);
    }

}
