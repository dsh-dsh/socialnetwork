package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
import com.skillbox.socialnet.model.RS.GeneralResponse;
import com.skillbox.socialnet.model.dto.MessageOkDTO;
import com.skillbox.socialnet.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @author Semen V
 * @created 18|11|2021
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<GeneralResponse<MessageOkDTO>> register(@RequestBody AccountRegisterRQ accountRegisterRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.register(accountRegisterRQ));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password/recovery")
    public ResponseEntity<?> passwordRecovery(
            @RequestBody @Valid AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.recoveryPassword(accountEmailRQ, servletRequest));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password/set")
    public ResponseEntity<?> setPassword(@RequestBody AccountPasswordSetRQ accountPasswordSetRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setPassword(accountPasswordSetRQ));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/shift-email")
    public ResponseEntity<?> shiftEmail(HttpServletRequest servletRequest) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.shiftEmail(servletRequest));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/email")
    public ResponseEntity<?> setEmail(@RequestBody AccountEmailRQ accountEmailRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setEmail(accountEmailRQ));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/notifications")
    public ResponseEntity<?> setNotifications(@RequestBody AccountNotificationRQ accountNotificationRQ) {
        GeneralResponse<MessageOkDTO> response =
                new GeneralResponse<>(accountService.setNotifications(accountNotificationRQ));
        return ResponseEntity.ok(response);
    }


}
