package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.RQ.*;
import com.skillbox.socialnet.model.RS.DefaultRS;
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
    public ResponseEntity<?> register(@RequestBody AccountRegisterRQ accountRegisterRQ) {
        DefaultRS defaultRS = accountService.register(accountRegisterRQ);
        if (defaultRS.getError().equals("string")){
            return ResponseEntity.ok(defaultRS);
        }
        return new ResponseEntity<>(defaultRS, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/password/recovery")
    public ResponseEntity<?> passwordRecovery(
            @RequestBody @Valid AccountEmailRQ accountEmailRQ,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(accountService.recoveryPassword(accountEmailRQ, servletRequest));
    }

    @PutMapping("/password/set")
    public ResponseEntity<?> setPassword(@RequestBody AccountPasswordSetRQ accountPasswordSetRQ) {
        return ResponseEntity.ok(accountService.setPassword(accountPasswordSetRQ));
    }

    @PutMapping("/shift-email")
    public ResponseEntity<?> shiftEmail(HttpServletRequest servletRequest) {
        return ResponseEntity.ok(accountService.shiftEmail(servletRequest));
    }

    @PutMapping("/email")
    public ResponseEntity<?> setEmail(@RequestBody AccountEmailRQ accountEmailRQ) {
        return ResponseEntity.ok(accountService.setEmail(accountEmailRQ));
    }

    @PutMapping("/notifications")
    public ResponseEntity<?> setNotifications(@RequestBody AccountNotificationRQ accountNotificationRQ) {
        return ResponseEntity.ok(accountService.setNotifications(accountNotificationRQ));
    }


}
